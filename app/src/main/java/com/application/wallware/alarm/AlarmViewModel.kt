package com.application.wallware.alarm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AlarmViewModel : ViewModel() {


    private val alarmList = MutableLiveData<ArrayList<AlarmModel>>()
    private val listItems = ArrayList<AlarmModel>()
    private val TAG = AlarmViewModel::class.java.simpleName

    fun setListAlarm() {
        listItems.clear()


        try {
            Firebase.firestore.collection("alarm")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val userItem = AlarmModel()
                        userItem.uid = document.data["uid"].toString()
                        userItem.name = document.data["name"].toString()
                        userItem.category = document.data["category"].toString()
                        userItem.time = document.data["time"].toString().toInt()

                        listItems.add(userItem)
                    }
                    alarmList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getAlarmList() : LiveData<ArrayList<AlarmModel>> {
        return alarmList
    }
}