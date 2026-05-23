package com.example.flickfind

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.ui.Navigation.AuthRoute
import com.example.flickfind.ui.Navigation.HomeRoute
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
    val navController = rememberNavController()
    val remote = AppRemote()
    var auth = remote.creatFirebaseAuth()

    // Kiểm tra trạng thái đăng nhập để chọn màn hình khởi đầu
    val startDestination = if (auth.currentUser != null) {
        HomeRoute
    } else {
        AuthRoute
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 1. Luồng Auth
        composable<AuthRoute> {
            AuthScreen(
                onLoginSuccess = {
                    // Xóa màn hình Auth khỏi stack để không bị quay lại khi nhấn Back
                    navController.navigate(HomeRoute) {
                        popUpTo(AuthRoute) { inclusive = true }
                    }
                }
            )
        }

        // 2. Luồng Home
        composable<HomeRoute> {
            HomeScree(
                onLogout = {
                    auth.signOut()
                    navController.navigate(AuthRoute) {
                        popUpTo(HomeRoute) { inclusive = true }
                    }
                }
            )
        }
    }
}
