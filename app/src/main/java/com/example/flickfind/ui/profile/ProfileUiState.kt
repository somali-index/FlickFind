package com.example.flickfind.ui.profile

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val username: String = "",
    val avatar: String = "",
    val isPremium: Boolean = false,
    val savedMoviesCount: Int = 0,
    val watchedMoviesCount: Int = 0,
    val collectionsCount: Int = 0,
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val isChangePasswordDialogVisible: Boolean = false,
    val isChangeNameDialogVisible: Boolean = false,
    val isChangeUsernameDialogVisible: Boolean = false
)
