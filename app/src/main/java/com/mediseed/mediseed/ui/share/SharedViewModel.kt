package com.mediseed.mediseed.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    private val _nearDistance: MutableLiveData<Boolean> = MutableLiveData()
    val nearDistance: LiveData<Boolean> get() = _nearDistance

    fun setData(data:Boolean) {
        _nearDistance.value = data
    }
}