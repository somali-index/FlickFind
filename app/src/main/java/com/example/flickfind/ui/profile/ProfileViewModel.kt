package com.example.flickfind.ui.profile

import androidx.lifecycle.ViewModel
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.Remote.AppRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(application: android.app.Application) : androidx.lifecycle.AndroidViewModel(application) {

    private val remote = AppRemote()
    // Lấy movieDao từ AppDatabase
    private val movieDao = com.example.flickfind.DATALAYER.Room.AppDatabase.getDatabase(application).movieDao()
    private val repository = Repository(remote, movieDao)
    private val auth = remote.creatFirebaseAuth()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        val currentUser = auth.currentUser
        
        if (currentUser != null) {
            // 1. Lấy Email từ Firebase Auth
            val userEmail = currentUser.email ?: ""
            _uiState.update { it.copy(email = userEmail) }

            // 2. Lấy Name và Avatar từ Firestore bằng Email thay vì UID
            repository.getUserProfile(userEmail) { name, avatar ->
                _uiState.update {
                    it.copy(
                        name = name,
                        avatar = if (avatar.isNotEmpty()) avatar else it.avatar
                    )
                }
            }
        }
    }
}
