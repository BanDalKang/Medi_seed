package com.mediseed.mediseed.data.remote

import com.mediseed.mediseed.BuildConfig
import com.mediseed.mediseed.data.model.DaejeonSeoguResponse
import com.mediseed.mediseed.data.model.DaejeonYuseongguResponse
import com.mediseed.mediseed.data.model.GeoCodeResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PharmacyDataSource {
    @GET("15077806/v1/uddi:1207b449-cc87-4c0d-93d7-d9dfae695a22")
    suspend fun getPharmacyDaejeonSeogu(
        @Query("page") pageIndex: Int = 1,
        @Query("perPage") pageSize: Int = 200  ,
        @Query("serviceKey") apiKey: String = BuildConfig.PUBLIC_DATA_PHARMACY_DECODING
    ) : DaejeonSeoguResponse

    @GET("15078180/v1/uddi:d4d649cd-b320-4b97-b237-bd95b330eea9")
    suspend fun getDaejeonYuseonggu(
        @Query("page") pageIndex: Int = 1,
        @Query("perPage") pageSize: Int = 40 ,
        @Query("serviceKey") apiKey: String = BuildConfig.PUBLIC_DATA_PHARMACY_DECODING
    ) : DaejeonYuseongguResponse
}

interface GeoCodeDataSource {
    @GET("map-geocode/v2/geocode")
    suspend fun getGeoCode(
        @Query("주소") addresse: String?,
        //Header는 Interceptor에서 관리됩니다.
    ): GeoCodeResponse
}