package com.flickfind.app.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * DataStoreManager – Lưu trữ dữ liệu người dùng bằng DataStore Preferences.
 * Thay thế SharedPreferences để lưu favorites và user settings.
 *
 * Yêu cầu BTL [B]: DataStore lưu cài đặt người dùng.
 */

// Extension property để tạo DataStore singleton theo context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "flickfind_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        private const val TAG = "DataStoreManager"
        private val KEY_FAVORITES = stringPreferencesKey("favorites")
        private val KEY_DISPLAY_NAME = stringPreferencesKey("display_name")
        private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    // ─── Favorites (lưu local, set các movie ID) ───────────────────────────

    /** Flow danh sách ID phim yêu thích */
    val favoritesFlow: Flow<Set<Int>> = context.dataStore.data.map { prefs ->
        val raw = prefs[KEY_FAVORITES] ?: ""
        if (raw.isBlank()) emptySet()
        else raw.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
    }

    /** Lấy favorites một lần (suspend) */
    suspend fun getFavoriteIds(): Set<Int> = favoritesFlow.first()

    /** Thêm phim vào yêu thích */
    suspend fun addFavorite(movieId: Int) {
        context.dataStore.edit { prefs ->
            val current = parseFavorites(prefs[KEY_FAVORITES])
            current.add(movieId)
            prefs[KEY_FAVORITES] = current.joinToString(",")
            Log.d(TAG, "addFavorite: id=$movieId, total=${current.size}")
        }
    }

    /** Xóa phim khỏi yêu thích */
    suspend fun removeFavorite(movieId: Int) {
        context.dataStore.edit { prefs ->
            val current = parseFavorites(prefs[KEY_FAVORITES])
            current.remove(movieId)
            prefs[KEY_FAVORITES] = current.joinToString(",")
            Log.d(TAG, "removeFavorite: id=$movieId, remaining=${current.size}")
        }
    }

    /** Kiểm tra phim có trong yêu thích không */
    suspend fun isFavorite(movieId: Int): Boolean = getFavoriteIds().contains(movieId)

    /** Toggle yêu thích – trả về true nếu đã thêm, false nếu đã xóa */
    suspend fun toggleFavorite(movieId: Int): Boolean {
        return if (isFavorite(movieId)) {
            removeFavorite(movieId)
            false
        } else {
            addFavorite(movieId)
            true
        }
    }

    // ─── User Settings ──────────────────────────────────────────────────────

    /** Flow tên hiển thị người dùng */
    val displayNameFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_DISPLAY_NAME] ?: ""
    }

    /** Lưu tên hiển thị tuỳ chỉnh */
    suspend fun saveDisplayName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DISPLAY_NAME] = name
        }
    }

    /** Flow trạng thái Dark Mode (mặc định false / theo hệ thống thì tuỳ ý, ở đây mặc định false) */
    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_DARK_MODE] ?: false
    }

    /** Lưu trạng thái Dark Mode */
    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DARK_MODE] = isDark
        }
    }

    // ─── Helper ─────────────────────────────────────────────────────────────

    private fun parseFavorites(raw: String?): MutableSet<Int> {
        if (raw.isNullOrBlank()) return mutableSetOf()
        return raw.split(",").mapNotNull { it.trim().toIntOrNull() }.toMutableSet()
    }
}
