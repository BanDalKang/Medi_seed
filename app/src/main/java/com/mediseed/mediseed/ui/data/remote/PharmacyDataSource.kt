package com.mediseed.mediseed.ui.data.remote

import com.mediseed.mediseed.BuildConfig
import com.mediseed.mediseed.ui.data.model.PharmacyResponce
import retrofit2.http.GET
import retrofit2.http.Query

interface PharmacyDataSource {

    @GET("15077806/v1/uddi:1207b449-cc87-4c0d-93d7-d9dfae695a22")
    suspend fun getPharmacy(
        @Query("page") pageIndex: Int = 1,
        @Query("perPage") pageSize: Int = 50 ,
        @Query("serviceKey") apiKey: String = BuildConfig.PUBLIC_DATA_PHARMACY_DECODING
    ) : PharmacyResponce
}