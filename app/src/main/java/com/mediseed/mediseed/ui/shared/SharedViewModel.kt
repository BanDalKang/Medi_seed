package com.mediseed.mediseed.ui.shared

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mediseed.mediseed.Const
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem

class SharedViewModel(private val pref: SharedPreferences) : ViewModel() {

    private val database = FirebaseDatabase.getInstance()

    private val _likedItems = MutableLiveData<List<PharmacyItem.PharmacyInfo>>(listOf())
    val likedItems: LiveData<List<PharmacyItem.PharmacyInfo>> get() = _likedItems

    private val _heartCount = MutableLiveData<Int>()
    val heartCount: LiveData<Int> get() = _heartCount

    private val _medicineCount = MutableLiveData<Int>()
    val medicineCount: LiveData<Int> get() = _medicineCount

    private val _updateHeartResult = MutableLiveData<Boolean>()
    val updateHeartResult: LiveData<Boolean> get() = _updateHeartResult

    init {
        loadLikedItems()
    }

    private fun loadLikedItems() {
        val jsonString = pref.getString(Const.LIKED_ITEMS, "")
        val items: List<PharmacyItem.PharmacyInfo> = if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            val type = object : TypeToken<List<PharmacyItem.PharmacyInfo>>() {}.type
            Gson().fromJson(jsonString, type)
        }
        _likedItems.value = items
    }

    fun addLikedItem(item: PharmacyItem.PharmacyInfo) {
        val currentItems = _likedItems.value?.toMutableList() ?: mutableListOf()
        if (!currentItems.contains(item)) {
            currentItems.add(item)
            _likedItems.value = currentItems
            saveLikedItems(currentItems)
        }
    }

    fun removeLikedItem(item: PharmacyItem.PharmacyInfo) {
        val currentItems = _likedItems.value?.toMutableList() ?: mutableListOf()
        if (currentItems.contains(item)) {
            currentItems.remove(item)
            _likedItems.value = currentItems
            saveLikedItems(currentItems)
        }
    }

    private fun saveLikedItems(items: List<PharmacyItem.PharmacyInfo>) {
        val jsonString = Gson().toJson(items)
        pref.edit().putString(Const.LIKED_ITEMS, jsonString).apply()
    }

    fun fetchHeartCount(address: String) {
        val ref = database.getReference("location/$address/heartCount")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val count = dataSnapshot.getValue(Int::class.java) ?: 0
                _heartCount.value = count
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
            }
        })
    }

    fun fetchMedicineCount(address: String) {
        val ref = database.getReference("location/$address/medicineCount")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val count = dataSnapshot.getValue(Int::class.java) ?: 0
                _medicineCount.value = count
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
            }
        })
    }

    fun updateHeartCount(address: String, increment: Boolean, facilityName: String) {
        val ref = database.getReference("location/$address/heartCount")
        ref.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                var currentHeartCount = mutableData.getValue(Int::class.java)
                if (currentHeartCount == null) {
                    currentHeartCount = 0
                }
                mutableData.value = if (increment) currentHeartCount + 1 else currentHeartCount - 1
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                committed: Boolean,
                dataSnapshot: DataSnapshot?
            ) {
                if (committed) {
                    _heartCount.value = dataSnapshot?.getValue(Int::class.java)
                    updateFacilityName(address, facilityName) { success ->
                        _updateHeartResult.value = success
                    }
                } else {
                    _updateHeartResult.value = false
                }
            }
        })
    }

    private fun updateFacilityName(address: String, facilityName: String, callback: (Boolean) -> Unit) {
        val ref = database.getReference("location/$address/facilityName")
        ref.setValue(facilityName).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    fun isPharmacyInfoLiked(pharmacyInfo: PharmacyItem.PharmacyInfo): Boolean {
        val likedItems = _likedItems.value ?: emptyList()
        return likedItems.any { it.streetNameAddress == pharmacyInfo.streetNameAddress }
    }

    class Factory(private val pref: SharedPreferences) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
                return SharedViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}