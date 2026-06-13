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

class CollectionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(AppRemote(), AppDatabase.getDatabase(application).movieDao())
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _collections = MutableStateFlow<List<DataCollection>>(emptyList())
    val collections = _collections.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchCollections()
    }

    fun fetchCollections() {
        val userId = auth.currentUser?.uid ?: return
        _isLoading.value = true
        
        // Danh sách các tên "hệ thống" cũ cần lọc bỏ để tránh rác
        val forbiddenNames = listOf("Danh sách đã lưu", "LƯU NHANH", "Quick Save")

        db.collection("Collections")
            .whereEqualTo("IDUser", userId)
            .addSnapshotListener { snapshot, _ ->
                val allCollections = snapshot?.documents?.mapNotNull { it.toObject(DataCollection::class.java) } ?: emptyList()
                
                // Lọc bỏ các thư mục hệ thống để chỉ hiện bộ sưu tập do người dùng tạo
                val filteredList = allCollections.filter { collection ->
                    !forbiddenNames.any { forbidden -> 
                        collection.CollectionName.trim().equals(forbidden, ignoreCase = true) 
                    }
                }
                
                _collections.value = filteredList
                _isLoading.value = false
            }
    }

    fun createCollection(name: String) {
        val userId = auth.currentUser?.uid ?: return
        if (name.isBlank()) return

        val id = db.collection("Collections").document().id
        val newCollection = DataCollection(
            IDCollection = id,
            CollectionName = name,
            IDUser = userId
        )

        db.collection("Collections").document(id).set(newCollection)
            .addOnSuccessListener {
                android.util.Log.d("CHECK_DATA", "Tạo bộ sưu tập thành công: $name")
            }
    }
}
