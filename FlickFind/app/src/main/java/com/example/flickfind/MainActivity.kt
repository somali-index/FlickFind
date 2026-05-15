package com.example.flickfind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.flickfind.ui.auth.AuthScreen
import com.example.flickfind.ui.home.HomeScreen
import com.example.flickfind.ui.theme.FlickFindTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlickFindTheme {
                FlickFindApp()
            }
        }
    }
}

@Composable
fun FlickFindApp() {
    // Trạng thái đăng nhập — lưu trong remember (chỉ tồn tại trong session)
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        HomeScreen(
            onLogout = { isLoggedIn = false }
        )
    } else {
        AuthScreen(
            onLoginSuccess = { isLoggedIn = true }
        )
    }
}
