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
        Log.d(
            "ActivitySearch",
            "onStart: Activity Search được khởi tạo ${this.hashCode()}"
        )

        enableEdgeToEdge()

        setContent {
            MovieSearchScreen()
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d(
            "ActivitySearch",
            "onStart: Activity Search bắt đầu hiển thị ${this.hashCode()}"
        )
    }

    override fun onResume() {
        super.onResume()

        Log.d(
            "ActivitySearch",
            "onResume: Người dùng có thể tương tác ${this.hashCode()}"
        )
    }

    override fun onPause() {
        super.onPause()

        Log.d(
            "ActivitySearch",
            "onPause: Activity Search tạm dừng ${this.hashCode()}"
        )
    }

    override fun onStop() {
        super.onStop()

        Log.d(
            "ActivitySearch",
            "onStop: Activity Search không còn hiển thị ${this.hashCode()}"
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(
            "ActivitySearch",
            "onDestroy: Activity đã bị hủy ${this.hashCode()}"
        )
    }
}