package com.example.flickfind.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.flickfind.DATALAYER.AppRepository.Repository
import com.example.flickfind.DATALAYER.DataClass.DataCollection
import com.example.flickfind.DATALAYER.Remote.AppRemote
import com.example.flickfind.DATALAYER.Room.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CollectionsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(
        remote = AppRemote(),
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _collections = MutableStateFlow<List<DataCollection>>(emptyList())
    val collections = _collections.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage = _userMessage.asStateFlow()

    // Quy tắc lọc rác: Các thư mục này sẽ không hiển thị trên giao diện Bộ sưu tập
    private val forbiddenNames = listOf("Danh sách đã lưu", "LƯU NHANH", "Quick Save", "Saved Movies", "Default")

    init {
        fetchCollections()
    }

    fun fetchCollections() {
        val currentUser = auth.currentUser ?: return
        db.collection("Collections")
            .whereEqualTo("IDUser", currentUser.email) // Dùng email làm IDUser theo Repository
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                
                val list = snapshot?.documents?.mapNotNull { it.toObject(DataCollection::class.java) } ?: emptyList()
                
                // Áp dụng bộ lọc rác
                val filteredList = list.filter { col ->
                    !forbiddenNames.any { forbidden -> 
                        col.CollectionName.trim().equals(forbidden, ignoreCase = true) 
                    }
                }
                _collections.update { filteredList }
            }
    }

    fun createCollection(name: String) {
        val currentUser = auth.currentUser ?: return
        val trimmedName = name.trim()

        if (trimmedName.isEmpty()) {
            _userMessage.update { "Tên bộ sưu tập không được để trống" }
            return
        }

        // Kiểm tra xem tên có nằm trong danh sách cấm không
        if (forbiddenNames.any { it.equals(trimmedName, ignoreCase = true) }) {
            _userMessage.update { "Tên này đã được sử dụng bởi hệ thống" }
            return
        }

        val id = db.collection("Collections").document().id
        val newCol = DataCollection(
            IDCollection = id,
            IDUser = currentUser.email ?: "", // Dùng email
            CollectionName = trimmedName
        )

        db.collection("Collections").document(id).set(newCol)
            .addOnSuccessListener {
                _userMessage.update { "Đã tạo bộ sưu tập '$trimmedName'" }
            }
            .addOnFailureListener {
                _userMessage.update { "Lỗi khi tạo bộ sưu tập" }
            }
    }

    fun clearMessage() {
        _userMessage.update { null }
    }
}
