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

    init {
        fetchUserProfile()
        observeSavedMoviesCount()
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

    private fun observeSavedMoviesCount() {
        viewModelScope.launch {
            repository.getAllSavedMoviesFlow().collectLatest { movies ->
                _uiState.update { it.copy(savedMoviesCount = movies.size) }
            }
        }
    }

    private fun fetchUserProfile() {
        val currentUser = auth.currentUser
        
        if (currentUser != null) {
            val userEmail = currentUser.email ?: ""
            _uiState.update { it.copy(email = userEmail) }

            repository.getUserProfile(userEmail) { name, username, _ ->
                // Tự động tạo URL ảnh ngẫu nhiên dựa trên thời gian hiện tại (phiên đăng nhập)
                // Ảnh này sẽ giữ nguyên trong suốt vòng đời của ViewModel này
                val randomAvatarUrl = "https://picsum.photos/seed/${userEmail}_${System.currentTimeMillis()}/200"
                
                _uiState.update {
                    it.copy(
                        name = name,
                        username = if (username.isNotEmpty()) "@$username" else it.username,
                        avatar = randomAvatarUrl
                    )
                }
            }
        }
    }
}
