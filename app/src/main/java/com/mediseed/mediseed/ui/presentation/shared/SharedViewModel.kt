package com.mediseed.mediseed.ui.presentation.shared

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mediseed.mediseed.ui.Const
import com.mediseed.mediseed.ui.presentation.home.model.PharmacyItem

class SharedViewModel(private val pref: SharedPreferences) : ViewModel() {

    private val database = FirebaseDatabase.getInstance()

    private val _likedItems = MutableLiveData<List<PharmacyItem.PharmacyInfo>>(listOf())
    val likedItems: LiveData<List<PharmacyItem.PharmacyInfo>> get() = _likedItems

    private val _heartCount = MutableLiveData<Int>()
    val heartCount: LiveData<Int> get() = _heartCount

    private val _medicineCount = MutableLiveData<Int>()
    val medicineCount: LiveData<Int> get() = _medicineCount

    private var heartCountListener: ValueEventListener? = null
    private var medicineCountListener: ValueEventListener? = null
    private var currentAddress: String? = null

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
        currentAddress = address

        heartCountListener?.let { ref.removeEventListener(it) }

        heartCountListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val count = dataSnapshot.getValue(Int::class.java) ?: 0
                _heartCount.value = count
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
            }
        }
        ref.addValueEventListener(heartCountListener as ValueEventListener)
    }

    fun fetchMedicineCount(address: String) {
        val ref = database.getReference("location/$address/medicineCount")
        currentAddress = address

        medicineCountListener?.let { ref.removeEventListener(it) }

        medicineCountListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val count = dataSnapshot.getValue(Int::class.java) ?: 0
                _medicineCount.value = count
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
            }
        }
        ref.addValueEventListener(medicineCountListener as ValueEventListener)
    }

    fun updateHeartCount(address: String, increment: Boolean, callback: (Boolean) -> Unit) {
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
                callback(committed)
                if (committed) {
                    _heartCount.value = dataSnapshot?.getValue(Int::class.java)
                }
            }
        })
    }

    fun isPharmacyInfoLiked(pharmacyInfo: PharmacyItem.PharmacyInfo): Boolean {
        val likedItems = _likedItems.value ?: emptyList()
        return likedItems.any { it.StreetNameAddress == pharmacyInfo.StreetNameAddress }
    }

    override fun onCleared() {
        super.onCleared()
        heartCountListener?.let { listener ->
            currentAddress?.let { address ->
                database.getReference("location/$address/heartCount").removeEventListener(listener)
            }
        }
        medicineCountListener?.let { listener ->
            currentAddress?.let { address ->
                database.getReference("location/$address/medicineCount").removeEventListener(listener)
            }
        }
    }

    class Factory(private val pref: SharedPreferences) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(SharedPreferences::class.java).newInstance(pref)
        }
    }
}
