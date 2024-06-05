package com.mediseed.mediseed.ui.presentation.mypage

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.mediseed.mediseed.ui.Const
import com.mediseed.mediseed.ui.presentation.home.model.PharmacyItem
import kotlinx.coroutines.launch

class MyPageViewModel(private val pref: SharedPreferences) : ViewModel() {

    //코루틴 스코프 여기서 씀
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    //좋아요 누른 아이템들 MutableLivaData
    private val _likedItems: MutableLiveData<List<PharmacyItem.PharmacyInfo>> = MutableLiveData()

    //LiveData 사용
    val likedItems: LiveData<List<PharmacyItem.PharmacyInfo>> get() = _likedItems


    //아이템리스트 업데이트
    fun updateItem(myPharmacyModel: PharmacyItem.PharmacyInfo) {
        viewModelScope.launch {

        }
    }

    fun setValue() {

    }

    //좋아요 누른 아이템 가져오는 함수
    fun getLikedItems(): List<PharmacyItem.PharmacyInfo> {

        val jsonString = pref.getString(Const.LIKED_ITEMS, "")
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(
                jsonString,
                object : TypeToken<List<PharmacyItem.PharmacyInfo>>() {}.type
            )
        }
    }


    //ViewModel Factory , Sharedpreferences 사용
    class MyPageViewModelFactory(private val pref: SharedPreferences) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
                return MyPageViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
