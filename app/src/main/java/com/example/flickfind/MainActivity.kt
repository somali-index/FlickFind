package com.example.flickfind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.flickfind.ui.auth.AuthScreen
import com.example.flickfind.ui.home.LoginScreen
import com.example.flickfind.ui.home.LoginScreen
import com.example.flickfind.ui.theme.FlickFindTheme
import com.google.firebase.auth.FirebaseAuth

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

    var isLoggedIn by remember {
        mutableStateOf(
            FirebaseAuth.getInstance().currentUser != null
        )
    }

    if (isLoggedIn) {

        LoginScreen (
            onLogout = {

                FirebaseAuth.getInstance().signOut()
                isLoggedIn = false
            }
        )

    } else {

        AuthScreen(
            onLoginSuccess = {
                isLoggedIn = true
            }
        )
    }
}
