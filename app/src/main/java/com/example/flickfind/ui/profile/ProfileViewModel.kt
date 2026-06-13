package com.example.flickfind.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val remote = AppRemote()
    private val movieDao = AppDatabase.getDatabase(application).movieDao()
    private val repository = Repository(remote, movieDao)
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        val currentUser = auth.currentUser ?: return
        val userEmail = currentUser.email ?: ""
        
        _uiState.update { 
            it.copy(
                email = userEmail,
                username = "@${userEmail.split("@")[0]}",
                avatar = "https://i.pravatar.cc/300?u=$userEmail"
            ) 
        }

        // 1. Lấy thông tin User (Local & Remote)
        viewModelScope.launch {
            repository.getLocalUser()?.let { localUser ->
                _uiState.update {
                    it.copy(
                        name = localUser.UserName,
                        avatar = if (localUser.avatar.isNotEmpty()) localUser.avatar else "https://i.pravatar.cc/300?u=${localUser.Email}"
                    )
                }
            }
        }

        repository.getUserProfile(userEmail) { name, avatar ->
            _uiState.update {
                it.copy(
                    name = name,
                    avatar = if (avatar.isNotEmpty()) avatar else "https://i.pravatar.cc/300?u=$userEmail"
                )
            }
        }

        // 2. Đếm số lượng Phim trong "Lưu nhanh" (Room)
        viewModelScope.launch {
            repository.getAllSavedMoviesFlow().collect { movies ->
                _uiState.update { it.copy(quickSaveCount = movies.size) }
            }
        }

        // 3. Đếm số lượng Bộ sưu tập từ Firestore (Có lọc rác)
        val forbiddenNames = listOf("Danh sách đã lưu", "LƯU NHANH", "Quick Save")
        db.collection("Collections")
            .whereEqualTo("IDUser", currentUser.uid)
            .addSnapshotListener { snapshot, _ ->
                val allCount = snapshot?.documents?.count { doc ->
                    val name = doc.getString("CollectionName") ?: ""
                    !forbiddenNames.any { forbidden -> 
                        name.trim().equals(forbidden, ignoreCase = true) 
                    }
                } ?: 0
                _uiState.update { it.copy(collectionsCount = allCount) }
            }
    }
}
