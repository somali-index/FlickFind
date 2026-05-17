package com.example.flickfind.ui.auth

data class AuthUiState(
    // Dữ liệu người dùng nhập
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullName: String = "",

    // Chế độ hiển thị
    val isLoginMode: Boolean = true,
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,

    // Thông báo lỗi (null = không có lỗi)
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val fullNameError: String? = null,
    val generalError: String? = null,

    // Kết quả
    val isSuccess: Boolean = false
)
