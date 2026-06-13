package com.example.flickfind.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val remote = AppRemote()
    private val movieDao = AppDatabase.getDatabase(application).movieDao()
    private val repository = Repository(remote, movieDao)
    private val auth = remote.creatFirebaseAuth()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val forbiddenNames = listOf("Danh sách đã lưu", "LƯU NHANH", "Quick Save", "Saved Movies", "Default")

    init {
        fetchUserProfile()
        observeQuickSaveCount()
        observeCollectionsCount()
    }

    fun showChangePasswordDialog(show: Boolean) {
        _uiState.update { it.copy(isChangePasswordDialogVisible = show) }
    }

    fun showChangeNameDialog(show: Boolean) {
        _uiState.update { it.copy(isChangeNameDialogVisible = show) }
    }

    fun showChangeUsernameDialog(show: Boolean) {
        _uiState.update { it.copy(isChangeUsernameDialogVisible = show) }
    }

    fun clearMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun updateName(newName: String) {
        val currentUser = auth.currentUser ?: return
        _uiState.update { it.copy(isLoading = true) }
        repository.updateUserProfile(currentUser.email ?: "", newName, null, null) { success ->
            _uiState.update {
                it.copy(
                    name = if (success) newName else it.name,
                    isLoading = false,
                    isChangeNameDialogVisible = false,
                    userMessage = if (success) "Cập nhật tên thành công" else "Lỗi cập nhật tên"
                )
            }
        }
    }

    fun updateUsername(newUsername: String) {
        val currentUser = auth.currentUser ?: return
        _uiState.update { it.copy(isLoading = true) }
        val cleanUsername = newUsername.removePrefix("@")
        repository.updateUserProfile(currentUser.email ?: "", null, cleanUsername, null) { success ->
            _uiState.update {
                it.copy(
                    username = if (success) "@$cleanUsername" else it.username,
                    isLoading = false,
                    isChangeUsernameDialogVisible = false,
                    userMessage = if (success) "Cập nhật username thành công" else "Lỗi cập nhật username"
                )
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        val user = auth.currentUser ?: return
        val email = user.email ?: return

        _uiState.update { it.copy(isLoading = true) }

        val credential = EmailAuthProvider.getCredential(email, currentPassword)

        user.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isChangePasswordDialogVisible = false,
                                        userMessage = "Đổi mật khẩu thành công"
                                    )
                                }
                            } else {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        userMessage = "Lỗi: ${updateTask.exception?.message}"
                                    )
                                }
                            }
                        }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Mật khẩu hiện tại không đúng"
                        )
                    }
                }
            }
    }

    private fun observeQuickSaveCount() {
        val userEmail = auth.currentUser?.email ?: return
        viewModelScope.launch {
            // Theo dõi phim Local
            val localMoviesFlow = repository.getAllSavedMoviesFlow()
            
            // Lấy phim Cloud từ bộ sưu tập "Default"
            repository.getDefaultCollectionId(userEmail) { collectionId ->
                if (collectionId != null) {
                    repository.getMoviesInCollection(collectionId) { cloudMovies ->
                        viewModelScope.launch {
                            localMoviesFlow.collectLatest { localMovies ->
                                val totalCount = (localMovies + cloudMovies).distinctBy { it.IDMovie }.size
                                _uiState.update { it.copy(quickSaveCount = totalCount) }
                            }
                        }
                    }
                } else {
                    // Nếu chưa có Default collection, chỉ hiện số lượng local
                    viewModelScope.launch {
                        localMoviesFlow.collectLatest { localMovies ->
                            _uiState.update { it.copy(quickSaveCount = localMovies.size) }
                        }
                    }
                }
            }
        }
    }

    private fun observeCollectionsCount() {
        val userEmail = auth.currentUser?.email ?: return
        repository.fetchUserCollections(userEmail) { collections ->
            val filteredCollections = collections.filter { collection ->
                forbiddenNames.none { forbidden ->
                    collection.CollectionName.contains(forbidden, ignoreCase = true)
                }
            }
            _uiState.update { it.copy(collectionsCount = filteredCollections.size) }
        }
    }

    private fun fetchUserProfile() {
        val currentUser = auth.currentUser
        
        if (currentUser != null) {
            val userEmail = currentUser.email ?: "blackpama110821@gmail.com" // Ưu tiên email đang login
            _uiState.update { it.copy(email = userEmail) }

            repository.getUserProfile(userEmail) { name, username, avatarUrl ->
                _uiState.update {
                    it.copy(
                        name = if (name.isNotEmpty()) name else "Người dùng",
                        username = if (username.isNotEmpty()) "@$username" else "@user",
                        avatar = if (avatarUrl.isNotEmpty()) avatarUrl else "https://picsum.photos/seed/$userEmail/200"
                    )
                }
            }
        }
    }
}
