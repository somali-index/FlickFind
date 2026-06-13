package com.example.flickfind.ui.profile

data class ProfileUiState(
    val name: String = "Người dùng",
    val email: String = "",
    val username: String = "@user",
    val avatar: String = "",
    val isPremium: Boolean = false,
    val quickSaveCount: Int = 0, // Số lượng phim lưu nhanh (Local)
    val watchedMoviesCount: Int = 0,
    val collectionsCount: Int = 0, // Số lượng bộ sưu tập (Cloud - đã lọc)
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val isChangePasswordDialogVisible: Boolean = false,
    val isChangeNameDialogVisible: Boolean = false,
    val isChangeUsernameDialogVisible: Boolean = false
)
