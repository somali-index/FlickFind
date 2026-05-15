package com.example.flickfind.ui.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    // Backing property: _uiState chỉ sửa được trong ViewModel
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // ── Cập nhật các trường nhập liệu ──────────────────────────────

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, emailError = null) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, passwordError = null) }
    }

    fun onConfirmPasswordChange(newConfirm: String) {
        _uiState.update { it.copy(confirmPassword = newConfirm, confirmPasswordError = null) }
    }

    fun onFullNameChange(newName: String) {
        _uiState.update { it.copy(fullName = newName, fullNameError = null) }
    }

    // ── Hiện / ẩn mật khẩu ────────────────────────────────────────

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    // ── Chuyển giữa Đăng nhập và Đăng ký ─────────────────────────

    fun toggleAuthMode() {
        // Reset toàn bộ state, chỉ đổi isLoginMode
        _uiState.update { AuthUiState(isLoginMode = !it.isLoginMode) }
    }

    // ── Validate dữ liệu ──────────────────────────────────────────

    private fun validateLoginInput(): Boolean {
        val state = _uiState.value
        var isValid = true

        if (state.email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email không được để trống") }
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.update { it.copy(emailError = "Email không đúng định dạng") }
            isValid = false
        }

        if (state.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Mật khẩu không được để trống") }
            isValid = false
        } else if (state.password.length < 6) {
            _uiState.update { it.copy(passwordError = "Mật khẩu phải có ít nhất 6 ký tự") }
            isValid = false
        }

        return isValid
    }

    private fun validateRegisterInput(): Boolean {
        var isValid = validateLoginInput()
        val state = _uiState.value

        if (state.fullName.isBlank()) {
            _uiState.update { it.copy(fullNameError = "Vui lòng nhập họ và tên") }
            isValid = false
        }

        if (state.confirmPassword.isBlank()) {
            _uiState.update { it.copy(confirmPasswordError = "Vui lòng xác nhận mật khẩu") }
            isValid = false
        } else if (state.confirmPassword != state.password) {
            _uiState.update { it.copy(confirmPasswordError = "Mật khẩu xác nhận không khớp") }
            isValid = false
        }

        return isValid
    }

    // ── Xử lý Đăng nhập ──────────────────────────────────────────

    fun onLoginClick() {
        if (!validateLoginInput()) return

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        // Tài khoản test mặc định (thay bằng API thực tế sau)
        val state = _uiState.value
        if (state.email == "test@flickfind.com" && state.password == "123456") {
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    generalError = "Email hoặc mật khẩu không đúng\n(Thử: test@flickfind.com / 123456)"
                )
            }
        }
    }

    // ── Xử lý Đăng ký ────────────────────────────────────────────

    fun onRegisterClick() {
        if (!validateRegisterInput()) return

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        // Giả lập đăng ký thành công (thay bằng API thực tế sau)
        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
    }
}
