package com.example.flickfind.ui.profile

data class ProfileUiState(
    val name: String = "Người dùng",
    val email: String = "",
    val username: String = "",
    val avatar: String = "",
    val isPremium: Boolean = false,
    val watchedMoviesCount: Int = 0,
    val collectionsCount: Int = 0,
    val quickSaveCount: Int = 0
)
