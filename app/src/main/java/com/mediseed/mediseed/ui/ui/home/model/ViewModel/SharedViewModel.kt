package com.mediseed.mediseed.ui.ui.home.model.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    private var _nearDistance: MutableLiveData<Boolean> = MutableLiveData()
    val nearDistance: LiveData<Boolean> get() = _nearDistance


    init {
        _nearDistance = MutableLiveData(false)
    }

    fun setData(data:Boolean) {
        _nearDistance.value = data
    }
}