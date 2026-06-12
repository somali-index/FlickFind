package com.example.flickfind.DATALAYER.Remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class AppRemote {
    private val FSDatabase = FirebaseFirestore.getInstance()
    private val FBAuth = FirebaseAuth.getInstance()
    
    // Sử dụng URL bucket cụ thể từ google-services.json của bạn
    private val FBStorage = FirebaseStorage.getInstance("gs://flickfind-5d618.firebasestorage.app")


    fun creatFirebaseAuth(): FirebaseAuth {
        return FBAuth
    }

    fun creatRemoteFS(): FirebaseFirestore {
        return FSDatabase
    }

    fun creatRemoteStorage(): FirebaseStorage {
        return FBStorage
    }
}
