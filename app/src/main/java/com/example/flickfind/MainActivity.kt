package com.example.flickfind

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.flickfind.ui.auth.AuthScreen
import com.example.flickfind.ui.home.HomeScree
import com.example.flickfind.ui.home.HomeScree
import com.example.flickfind.ui.theme.FlickFindTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFECYCLE","onCreate: MainActivity được tạo ${this.hashCode()}")

        enableEdgeToEdge()
        setContent {

            FlickFindTheme {
                FlickFindApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d("LIFECYCLE","onStart: Activity bắt đầu hiển thị ${this.hashCode()}")
    }

    override fun onResume() {
        super.onResume()

        Log.d("LIFECYCLE","onResume: Người dùng có thể tương tác ${this.hashCode()}")
    }

    override fun onPause() {
        super.onPause()

        Log.d("LIFECYCLE","onPause: Activity tạm dừng ${this.hashCode()}")
    }

    override fun onStop() {
        super.onStop()

        Log.d("LIFECYCLE","onStop: Activity không còn hiển thị ${this.hashCode()}")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("MainActivity","onAttachedToWindow from MainActivity ${this.hashCode()}")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        Log.d("MainActivity","onDetachedFromWindow from MainActivity ${this.hashCode()}")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("MainActivity","Activity đã bị hủy ${this.hashCode()}")
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

        HomeScree(
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
