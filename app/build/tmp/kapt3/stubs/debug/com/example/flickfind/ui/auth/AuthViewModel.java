package com.example.flickfind.ui.auth;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u0010J\u000e\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0010J\u000e\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u0010J\u0006\u0010\u0017\u001a\u00020\u000eJ\u0006\u0010\u0018\u001a\u00020\u000eJ\b\u0010\u0019\u001a\u00020\u001aH\u0002J\b\u0010\u001b\u001a\u00020\u001aH\u0002J\u0006\u0010\u001c\u001a\u00020\u000eJ\u0006\u0010\u001d\u001a\u00020\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u001e"}, d2 = {"Lcom/example/flickfind/ui/auth/AuthViewModel;", "Landroidx/lifecycle/ViewModel;", "<init>", "()V", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/flickfind/ui/auth/AuthUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "onEmailChange", "", "newEmail", "", "onPasswordChange", "newPassword", "onConfirmPasswordChange", "newConfirm", "onFullNameChange", "newName", "togglePasswordVisibility", "toggleAuthMode", "validateLoginInput", "", "validateRegisterInput", "onLoginClick", "onRegisterClick", "app_debug"})
public final class AuthViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.auth.FirebaseAuth auth = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.flickfind.ui.auth.AuthUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.flickfind.ui.auth.AuthUiState> uiState = null;
    
    public AuthViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.flickfind.ui.auth.AuthUiState> getUiState() {
        return null;
    }
    
    public final void onEmailChange(@org.jetbrains.annotations.NotNull()
    java.lang.String newEmail) {
    }
    
    public final void onPasswordChange(@org.jetbrains.annotations.NotNull()
    java.lang.String newPassword) {
    }
    
    public final void onConfirmPasswordChange(@org.jetbrains.annotations.NotNull()
    java.lang.String newConfirm) {
    }
    
    public final void onFullNameChange(@org.jetbrains.annotations.NotNull()
    java.lang.String newName) {
    }
    
    public final void togglePasswordVisibility() {
    }
    
    public final void toggleAuthMode() {
    }
    
    private final boolean validateLoginInput() {
        return false;
    }
    
    private final boolean validateRegisterInput() {
        return false;
    }
    
    public final void onLoginClick() {
    }
    
    public final void onRegisterClick() {
    }
}