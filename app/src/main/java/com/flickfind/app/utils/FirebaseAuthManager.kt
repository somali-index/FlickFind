package com.flickfind.app.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * FirebaseAuthManager – Quản lý xác thực người dùng qua Firebase Authentication.
 * Singleton object dùng chung toàn ứng dụng.
 */
object FirebaseAuthManager {

    private const val TAG = "FirebaseAuthManager"
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /** Người dùng hiện tại (null nếu chưa đăng nhập) */
    val currentUser: FirebaseUser? get() = auth.currentUser

    /** Kiểm tra trạng thái đăng nhập */
    val isLoggedIn: Boolean get() = auth.currentUser != null

    /** Email người dùng hiện tại */
    val userEmail: String get() = auth.currentUser?.email ?: ""

    /** Display name (dùng phần trước @ của email nếu chưa đặt tên) */
    val displayName: String
        get() {
            val name = auth.currentUser?.displayName
            if (!name.isNullOrBlank()) return name
            val email = auth.currentUser?.email ?: return "Cinephile"
            return email.substringBefore("@").replaceFirstChar { it.uppercase() }
        }

    /**
     * Đăng nhập bằng email + password.
     * @return Result.success(Unit) nếu thành công, Result.failure(Exception) nếu lỗi.
     */
    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d(TAG, "signIn: thành công với email=$email")
            Result.success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.w(TAG, "signIn: email chưa đăng ký", e)
            Result.failure(Exception("Email này chưa được đăng ký"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.w(TAG, "signIn: sai mật khẩu", e)
            Result.failure(Exception("Mật khẩu không đúng"))
        } catch (e: Exception) {
            Log.e(TAG, "signIn: lỗi không xác định", e)
            Result.failure(Exception("Đăng nhập thất bại: ${e.message}"))
        }
    }

    /**
     * Đăng ký tài khoản mới bằng email + password.
     * @return Result.success(Unit) nếu thành công, Result.failure(Exception) nếu lỗi.
     */
    suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Log.d(TAG, "signUp: tạo tài khoản thành công email=$email")
            Result.success(Unit)
        } catch (e: FirebaseAuthWeakPasswordException) {
            Log.w(TAG, "signUp: mật khẩu quá yếu", e)
            Result.failure(Exception("Mật khẩu quá yếu, vui lòng dùng ít nhất 6 ký tự"))
        } catch (e: FirebaseAuthUserCollisionException) {
            Log.w(TAG, "signUp: email đã tồn tại", e)
            Result.failure(Exception("Email này đã được đăng ký, vui lòng đăng nhập"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.w(TAG, "signUp: email không hợp lệ", e)
            Result.failure(Exception("Định dạng email không hợp lệ"))
        } catch (e: Exception) {
            Log.e(TAG, "signUp: lỗi không xác định", e)
            Result.failure(Exception("Đăng ký thất bại: ${e.message}"))
        }
    }

    /**
     * Đăng xuất người dùng hiện tại.
     */
    fun signOut() {
        Log.d(TAG, "signOut: đăng xuất user=${auth.currentUser?.email}")
        auth.signOut()
    }

    /**
     * Gửi email đặt lại mật khẩu.
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Log.d(TAG, "sendPasswordResetEmail: đã gửi đến $email")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "sendPasswordResetEmail: lỗi", e)
            Result.failure(Exception("Không thể gửi email đặt lại mật khẩu"))
        }
    }

    /**
     * Đổi mật khẩu.
     */
    suspend fun updatePassword(password: String): Result<Unit> {
        return try {
            auth.currentUser?.updatePassword(password)?.await()
            Log.d(TAG, "updatePassword: thành công")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "updatePassword: lỗi", e)
            Result.failure(Exception("Đổi mật khẩu thất bại: ${e.message}"))
        }
    }
}
