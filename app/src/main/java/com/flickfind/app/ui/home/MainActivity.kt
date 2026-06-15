package com.flickfind.app.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.flickfind.app.ui.navigation.FlickFindApp
import com.flickfind.app.ui.theme.FlickFindTheme
import com.flickfind.app.utils.DataStoreManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStoreManager = DataStoreManager(this)
        Log.d("MainActivity", "onCreate: Khởi tạo MainActivity (Compose)")
        setContent {
            val isDarkMode by dataStoreManager.darkModeFlow.collectAsState(initial = false)
            FlickFindTheme(darkTheme = isDarkMode) {
                FlickFindApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart: MainActivity bắt đầu hiển thị")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume: MainActivity tương tác được")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause: MainActivity mất focus")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop: MainActivity không còn visible")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy: MainActivity bị huỷ")
    }
}
