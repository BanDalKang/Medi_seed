package com.mediseed.mediseed.ui.share

import android.os.Parcelable

interface MarkerItem: Parcelable {

    val CollectionLocationName: String?

    val CollectionLocationClassificationName: String?

    val PhoneNumber: String?

    val DataDate: String?

    val lotNumberAddress: String?


}