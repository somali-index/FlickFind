package com.example.flickfind.DATALAYER.Remote
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore


class AppRemote {
    private val FSDatabase = FirebaseFirestore.getInstance();

    fun creatRemoteFS(): FirebaseFirestore{
        return  FSDatabase
    }
}