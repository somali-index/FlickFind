package com.example.flickfind.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(
        remote = AppRemote(),
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )

    private val _collections = MutableStateFlow<List<DataCollection>>(emptyList())
    val collections = _collections.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage = _userMessage.asStateFlow()

    private val _isSlowLoading = MutableStateFlow(false)
    val isSlowLoading = _isSlowLoading.asStateFlow()

    private val forbiddenNames = listOf("Danh sách đã lưu", "LƯU NHANH", "Quick Save", "Saved Movies", "Default")
    private var slowLoadingJob: Job? = null

    init {
        fetchCollections()
    }

    fun fetchCollections() {
        repository.listenCurrentUserCollections { list ->
            val filteredList = list.filter { col ->
                forbiddenNames.none { forbidden ->
                    col.CollectionName.trim().equals(forbidden, ignoreCase = true)
                }
            }
            _collections.update { filteredList }
        }
    }

    fun createCollection(name: String) {
        val trimmedName = name.trim()

        if (!validateCollectionName(trimmedName)) return

        startSlowLoadingTimer()
        repository.createCollectionForCurrentUser(trimmedName) { success ->
            stopSlowLoadingTimer()
            val message = if (success) {
                "Đã tạo bộ sưu tập '$trimmedName'"
            } else {
                "Lỗi khi tạo bộ sưu tập"
            }
            _userMessage.update { message }
        }
    }

    fun updateCollection(collectionId: String, name: String) {
        val trimmedName = name.trim()
        if (!validateCollectionName(trimmedName)) return

        startSlowLoadingTimer()
        repository.updateCollectionName(collectionId, trimmedName) { success ->
            stopSlowLoadingTimer()
            _userMessage.update {
                if (success) "Đã sửa tên bộ sưu tập" else "Lỗi khi sửa bộ sưu tập"
            }
        }
    }

    fun deleteCollection(collectionId: String, name: String) {
        startSlowLoadingTimer()
        repository.deleteCollection(collectionId) { success ->
            stopSlowLoadingTimer()
            _userMessage.update {
                if (success) "Đã xóa bộ sưu tập '$name'" else "Lỗi khi xóa bộ sưu tập"
            }
        }
    }

    fun clearMessage() {
        _userMessage.update { null }
    }

    private fun startSlowLoadingTimer() {
        slowLoadingJob?.cancel()
        _isSlowLoading.value = false
        slowLoadingJob = viewModelScope.launch {
            delay(3000)
            _isSlowLoading.value = true
        }
    }

    private fun stopSlowLoadingTimer() {
        slowLoadingJob?.cancel()
        slowLoadingJob = null
        _isSlowLoading.value = false
    }

    private fun validateCollectionName(name: String): Boolean {
        if (name.isEmpty()) {
            _userMessage.update { "Tên bộ sưu tập không được để trống" }
            return false
        }

        if (forbiddenNames.any { it.equals(name, ignoreCase = true) }) {
            _userMessage.update { "Tên này đã được hệ thống sử dụng" }
            return false
        }

        return true
    }
}
