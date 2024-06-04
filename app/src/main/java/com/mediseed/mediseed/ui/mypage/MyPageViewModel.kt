package com.mediseed.mediseed.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MyPageViewModel : ViewModel() {

    //코루틴 스코프 여기서 씀
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    //좋아요 누른 아이템들 MutableLivaData
    private val _likedItems: MutableLiveData<List<MyPharmacyModel>> = MutableLiveData()

    //LiveData 사용
    val likedItems: LiveData<List<MyPharmacyModel>> get() = _likedItems


    //좋아요 제거? -> 약국 아이템 눌렀을 경우 다른 페이지로 이동 안하면 여기서 못함.
    private fun removeItem(pharmacyItemModel: MyPharmacyModel) {

    }

    //아이템리스트 업데이트
    fun updateItem(myPharmacyModel: MyPharmacyModel) {
        viewModelScope.launch {

        }
    }


    //좋아요 누른 아이템 가져오는 함수
    fun getLikedItems() {
        viewModelScope.launch {
            //_likedItems.value = repository 사용
        }
    }

    //ViewModel Factory ?
    /*class MyPageViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

        }
    }*/

}