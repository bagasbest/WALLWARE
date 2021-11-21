package com.application.wallware.catalogue

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CatalogueViewModel: ViewModel() {
    private val consultantList = MutableLiveData<ArrayList<CatalogueModel>>()
    private val listItems = ArrayList<CatalogueModel>()
    private val TAG = CatalogueViewModel::class.java.simpleName

    fun setCatalogue() {
        listItems.clear()


        try {
            Firebase.firestore.collection("catalogue")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val userItem = CatalogueModel()
                        userItem.uid = document.data["uid"].toString()
                        userItem.name = document.data["name"].toString()
                        userItem.description = document.data["description"].toString()
                        userItem.image = document.data["image"].toString()
                        userItem.time = document.data["time"].toString().toInt()
                        userItem.category = document.data["category"].toString()

                        listItems.add(userItem)
                    }
                    consultantList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun setCatalogueByQuery(query: String) {
        listItems.clear()

        try {
            Firebase.firestore.collection("catalogue")
                .whereGreaterThanOrEqualTo("nameTemp", query)
                .whereLessThanOrEqualTo("nameTemp", query + '\uf8ff')
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val userItem = CatalogueModel()
                        userItem.uid = document.data["uid"].toString()
                        userItem.name = document.data["name"].toString()
                        userItem.description = document.data["description"].toString()
                        userItem.image = document.data["image"].toString()
                        userItem.time = document.data["time"].toString().toInt()
                        userItem.category = document.data["category"].toString()


                        listItems.add(userItem)
                    }
                    consultantList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getCatalogue() : LiveData<ArrayList<CatalogueModel>> {
        return consultantList
    }


}