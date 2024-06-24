package com.mediseed.mediseed.ui.home.model.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction

class SharedViewModel : ViewModel() {
    private var _nearDistance: MutableLiveData<Boolean> = MutableLiveData()
    val nearDistance: LiveData<Boolean> get() = _nearDistance

    private var _markerAddress: MutableLiveData<String> = MutableLiveData()
    val markerAddress: LiveData<String> get() = _markerAddress

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()


    init {
        _nearDistance = MutableLiveData(false)
    }

    fun setData(data: Boolean) {
        _nearDistance.value = data
    }

    fun setAddress(address: String) {
        _markerAddress.value = address
    }


    fun updateMedicineCount() {
        val address = _markerAddress.value ?: return
        val ref = database.getReference("location/$address/medicineCount")

        ref.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                var count = currentData.getValue(Int::class.java)
                if (count == null) {
                    count = 0
                }
                currentData.value = count + 1
                return Transaction.success(currentData)
            }

            override fun onComplete(databaseError: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                if (databaseError != null) {
                    Log.e("SharedViewModel", "Error incrementing medicine count: ${databaseError.message}")
                }
            }
        })
    }
}