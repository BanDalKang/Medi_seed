package com.mediseed.mediseed.ui.presentation.bottomSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class BottomSheetViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()

    private val _heartCount = MutableLiveData<Int>()
    val heartCount: LiveData<Int> get() = _heartCount

    private var heartCountListener: ValueEventListener? = null
    private var currentTurn: Int? = null

    fun fetchHeartCount(turn: Int) {
        val ref = database.getReference("location/$turn/heart")
        currentTurn = turn

        // 이전 리스너가 있으면 제거하여 메모리 누수 방지
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

    fun updateHeartCount(turn: Int, increment: Boolean, callback: (Boolean) -> Unit) {
        val ref = database.getReference("location/$turn/heart")

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
                    // 이 부분은 선택사항이며, 리스너가 값을 업데이트할 것이기 때문에
                    _heartCount.value = dataSnapshot?.getValue(Int::class.java)
                }
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel이 클리어될 때 리스너를 제거하여 메모리 누수 방지
        heartCountListener?.let { listener ->
            currentTurn?.let { turn ->
                database.getReference("location/$turn/heart").removeEventListener(listener)
            }
        }
    }
}