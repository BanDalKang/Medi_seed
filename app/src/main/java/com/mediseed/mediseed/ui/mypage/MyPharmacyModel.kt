package com.mediseed.mediseed.ui.mypage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyPharmacyModel(
    var title: String,
    var address: String,
    var kind: String
) : Parcelable
