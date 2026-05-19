package com.example.flickfind

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.flickfind.ui.SearchUI.MovieSearchScreen

class SearchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MovieSearchScreen()
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d(
            "Activity Search",
            "onStart: Activity Search bắt đầu hiển thị ${this.hashCode()}"
        )
    }

    override fun onResume() {
        super.onResume()

        Log.d(
            "Activity Search",
            "onResume: Người dùng có thể tương tác ${this.hashCode()}"
        )
    }

    override fun onPause() {
        super.onPause()

        Log.d(
            "Activity Search",
            "onPause: Activity Search tạm dừng ${this.hashCode()}"
        )
    }

    override fun onStop() {
        super.onStop()

        Log.d(
            "Activity Search",
            "onStop: Activity Search không còn hiển thị ${this.hashCode()}"
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(
            "Activity Search",
            "Activity đã bị hủy ${this.hashCode()}"
        )
    }
}