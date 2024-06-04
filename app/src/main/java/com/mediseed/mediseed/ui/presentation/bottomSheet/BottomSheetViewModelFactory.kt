package com.mediseed.mediseed.ui.presentation.bottomSheet

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BottomSheetViewModelFactory(private val pref: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BottomSheetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BottomSheetViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
