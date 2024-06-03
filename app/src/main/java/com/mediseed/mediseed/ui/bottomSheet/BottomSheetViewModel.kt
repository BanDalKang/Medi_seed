package com.mediseed.mediseed.ui.bottomSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class BottomSheetViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()

    private val _heartCount = MutableLiveData<Int>()
    val heartCount: LiveData<Int> get() = _heartCount

    private val _medicineCount = MutableLiveData<Int>()
    val medicineCount: LiveData<Int> get() = _medicineCount

    private var heartCountListener: ValueEventListener? = null
    private var medicineCountListener: ValueEventListener? = null
    private var currentTurn: Int? = null

    fun fetchHeartCount(turn: Int) {
        val ref = database.getReference("location/$turn/heartCount")
        currentTurn = turn

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

    fun fetchMedicineCount(turn: Int) {
        val ref = database.getReference("location/$turn/medicineCount")
        currentTurn = turn

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

    fun updateHeartCount(turn: Int, increment: Boolean, callback: (Boolean) -> Unit) {
        val ref = database.getReference("location/$turn/heartCount")

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

    override fun onCleared() {
        super.onCleared()
        heartCountListener?.let { listener ->
            currentTurn?.let { turn ->
                database.getReference("location/$turn/heartCount").removeEventListener(listener)
            }
        }
        medicineCountListener?.let { listener ->
            currentTurn?.let { turn ->
                database.getReference("location/$turn/medicineCount").removeEventListener(listener)
            }
        }
    }
}