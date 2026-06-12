package com.example.flickfind.DATALAYER.Remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AppRemote {
    private val FSDatabase = FirebaseFirestore.getInstance()
    private val FBAuth = FirebaseAuth.getInstance()
    
    fun creatFirebaseAuth(): FirebaseAuth {
        return FBAuth
    }

    fun creatRemoteFS(): FirebaseFirestore {
        return FSDatabase
    }
}
