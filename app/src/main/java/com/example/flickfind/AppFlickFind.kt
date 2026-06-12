package com.example.flickfind

import android.R.attr.tag
import android.app.Application
import android.nfc.Tag
import android.util.Log

import androidx.room.Room
import com.example.flickfind.DATALAYER.Room.AppDatabase

class AppFlickFind : Application() {
    
    // Khởi tạo database dùng chung cho toàn ứng dụng
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "flickfind_db"
        ).build()
    }

    override fun onCreate() {
        super.onCreate()
    }
}