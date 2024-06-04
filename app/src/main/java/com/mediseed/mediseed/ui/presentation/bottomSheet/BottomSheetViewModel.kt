package com.mediseed.mediseed.ui.presentation.bottomSheet

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mediseed.mediseed.ui.Const
import com.mediseed.mediseed.ui.presentation.home.model.PharmacyItem

class BottomSheetViewModel(private val pref: SharedPreferences) : ViewModel() {

    private val database = FirebaseDatabase.getInstance()

    private val _heartCount = MutableLiveData<Int>()
    val heartCount: LiveData<Int> get() = _heartCount

    private val _medicineCount = MutableLiveData<Int>()
    val medicineCount: LiveData<Int> get() = _medicineCount

    private var heartCountListener: ValueEventListener? = null
    private var medicineCountListener: ValueEventListener? = null
    private var currentAddress: String? = null

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

    private fun getPrefsItems(): List<PharmacyItem.PharmacyInfo> {
        val jsonString = pref.getString(Const.LIKED_ITEMS, "")
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(jsonString, object : TypeToken<List<PharmacyItem.PharmacyInfo>>() {}.type)
        }
    }

    private fun savePrefsItems(items: List<PharmacyItem.PharmacyInfo>) {
        val jsonString = Gson().toJson(items)
        pref.edit().putString(Const.LIKED_ITEMS, jsonString).apply()
    }

    fun savePharmacyInfoToPrefs(pharmacyInfo: PharmacyItem.PharmacyInfo) {
        val likedItems = getPrefsItems().toMutableList()
        val findItem = likedItems.find { it.StreetNameAddress == pharmacyInfo.StreetNameAddress }

        if (findItem == null) {
            likedItems.add(pharmacyInfo)
            savePrefsItems(likedItems)
        }
    }

    fun removePharmacyInfoFromPrefs(pharmacyInfo: PharmacyItem.PharmacyInfo) {
        val likedItems = getPrefsItems().toMutableList()
        likedItems.removeAll { it.StreetNameAddress == pharmacyInfo.StreetNameAddress }
        savePrefsItems(likedItems)
    }

    fun isPharmacyInfoLiked(pharmacyInfo: PharmacyItem.PharmacyInfo): Boolean {
        val likedItems = getPrefsItems()
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
}