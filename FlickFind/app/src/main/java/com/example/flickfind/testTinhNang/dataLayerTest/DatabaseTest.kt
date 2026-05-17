package com.example.flickfind.testTinhNang.dataLayerTest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest
import androidx.room.RoomDatabase
import com.example.flickfind.testTinhNang.dataLayerTest.DAO.DAOTest

@Database(
    entities = [UNITYTest::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): DAOTest

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}