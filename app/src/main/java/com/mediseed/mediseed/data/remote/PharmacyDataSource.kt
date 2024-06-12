package com.mediseed.mediseed.data.remote

import com.mediseed.mediseed.BuildConfig
import com.mediseed.mediseed.data.model.PharmacyDaejeonSeoguResponse
import com.mediseed.mediseed.data.model.PharmacyDaejeonYuseongguResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PharmacyDataSource {

    @GET("15077806/v1/uddi:1207b449-cc87-4c0d-93d7-d9dfae695a22")
    suspend fun getPharmacyDaejeonSeogu(
        @Query("page") pageIndex: Int = 1,
        @Query("perPage") pageSize: Int = 230  ,
        @Query("serviceKey") apiKey: String = BuildConfig.PUBLIC_DATA_PHARMACY_DECODING
    ) : PharmacyDaejeonSeoguResponse

    @GET("15078180/v1/uddi:d4d649cd-b320-4b97-b237-bd95b330eea9")
    suspend fun getDaejeonYuseonggu(
        @Query("page") pageIndex: Int = 1,
        @Query("perPage") pageSize: Int = 50 ,
        @Query("serviceKey") apiKey: String = BuildConfig.PUBLIC_DATA_PHARMACY_DECODING
    ) : PharmacyDaejeonYuseongguResponse

}