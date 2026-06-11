package com.example.flickfind.ui.profile

data class ProfileUiState(
    val name: String = "GAY NGUYÊN",
    val email: String = "abc@gmail.com",
    val username: String = "@Gay",
    val avatar: String = "https://i.pravatar.cc/300",
    val isPremium: Boolean = true,
    val savedMoviesCount: Int = 142,
    val watchedMoviesCount: Int = 48,
    val collectionsCount: Int = 5
)