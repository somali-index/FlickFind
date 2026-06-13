package com.example.flickfind.ui.auth

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(application: android.app.Application) : AndroidViewModel(application) {

    // Firebase Auth
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    private val repository = Repository(
        remote = AppRemote(),
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )

    // Backing property
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // ── Cập nhật input ───────────────────────────────────────────

    fun onEmailChange(newEmail: String) {
        _uiState.update {
            it.copy(
                email = newEmail,
                emailError = null
            )
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(
                password = newPassword,
                passwordError = null
            )
        }
    }

    fun onConfirmPasswordChange(newConfirm: String) {
        _uiState.update {
            it.copy(
                confirmPassword = newConfirm,
                confirmPasswordError = null
            )
        }
    }

    fun onFullNameChange(newName: String) {
        _uiState.update {
            it.copy(
                fullName = newName,
                fullNameError = null
            )
        }
    }

    // ── Password visibility ─────────────────────────────────────

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }

    // ── Chuyển mode login/register ──────────────────────────────

    fun toggleAuthMode() {
        _uiState.update {
            AuthUiState(
                isLoginMode = !it.isLoginMode
            )
        }
    }
    
    fun logout(){
        auth.signOut()
        repository.clearLocalUser() // Xóa dữ liệu user trong Room khi đăng xuất

        _uiState.update {
            AuthUiState(
                isLoginMode = true
            )
        }
    }
    
    fun resetSuccessState() {
        _uiState.update {
            it.copy(
                isSuccess = false
            )
        }
    }

    // ── Validate ────────────────────────────────────────────────

    private fun validateLoginInput(): Boolean {

        val state = _uiState.value
        var isValid = true
        Log.d("Login","Login đang được xử lý")

        if (state.email.isBlank()) {

            _uiState.update {
                it.copy(
                    emailError = "Email không được để trống"
                )
            }

            isValid = false
            Log.d("Login","EMAIL_ADDRESS resault = ${isValid}")


        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {

            _uiState.update {
                it.copy(
                    emailError = "Email không đúng định dạng"
                )
            }

            isValid = false
            Log.d("Login","EMAIL_ADDRESS resault = ${isValid}")

        }

        if (state.password.isBlank()) {

            _uiState.update {
                it.copy(
                    passwordError = "Mật khẩu không được để trống"
                )
            }

            isValid = false
            Log.d("Login","password resault = ${isValid}")


        } else if (state.password.length < 6) {

            _uiState.update {
                it.copy(
                    passwordError = "Mật khẩu phải có ít nhất 6 ký tự"
                )
            }

            isValid = false
            Log.d("Login","password resault = ${isValid}")

        }

        return isValid
    }

    private fun validateRegisterInput(): Boolean {

        var isValid = validateLoginInput()
        val state = _uiState.value

        if (state.fullName.isBlank()) {

            _uiState.update {
                it.copy(
                    fullNameError = "Vui lòng nhập họ và tên"
                )
            }

            isValid = false
        }

        if (state.confirmPassword.isBlank()) {

            _uiState.update {
                it.copy(
                    confirmPasswordError = "Vui lòng xác nhận mật khẩu"
                )
            }

            isValid = false

        } else if (state.confirmPassword != state.password) {

            _uiState.update {
                it.copy(
                    confirmPasswordError = "Mật khẩu xác nhận không khớp"
                )
            }

            isValid = false
        }

        return isValid
    }

    // ── LOGIN ───────────────────────────────────────────────────

    fun onLoginClick() {

        if (!validateLoginInput()) return

        val state = _uiState.value

        _uiState.update {
            it.copy(
                isLoading = true,
                generalError = null
            )
        }

        auth.signInWithEmailAndPassword(
            state.email,
            state.password
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Log.d("Login","Mật khẩu và Email đã đúng")
                
                // Fetch và save profile vào Room trước khi báo success
                repository.getUserProfile(state.email) { name, avatar ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                }

            } else {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = task.exception?.message
                            ?: "Đăng nhập thất bại"
                    )
                }
            }
        }
    }

    // ── REGISTER ────────────────────────────────────────────────

    fun onRegisterClick() {

        if (!validateRegisterInput()) return

        val state = _uiState.value

        _uiState.update {
            it.copy(
                isLoading = true,
                generalError = null
            )
        }

        auth.createUserWithEmailAndPassword(
            state.email,
            state.password
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                // Sau khi đăng ký thành công, tạo User trên Firestore
                val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                val userMap = hashMapOf(
                    "Email" to state.email,
                    "UserName" to state.fullName,
                    "Pass" to state.password,
                    "IDUser" to (auth.currentUser?.uid ?: ""),
                    "avatar" to ""
                )
                
                db.collection("User").add(userMap).addOnCompleteListener {
                     _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                }

            } else {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = task.exception?.message
                            ?: "Đăng ký thất bại"
                    )
                }
            }
        }
    }
}
