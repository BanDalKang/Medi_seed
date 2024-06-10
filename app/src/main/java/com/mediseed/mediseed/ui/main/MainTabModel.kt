package com.mediseed.mediseed.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

data class MainTabModel(
    val fragment: Fragment,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
)