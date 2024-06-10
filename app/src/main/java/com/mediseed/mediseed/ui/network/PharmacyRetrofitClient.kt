package com.mediseed.mediseed.ui.network

import com.mediseed.mediseed.ui.data.remote.PharmacyDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PharmacyRetrofitClient {

    private const val PHARMACY_API_BASE_URL = "https://api.odcloud.kr/api/"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(PHARMACY_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pharmacyDataSource: PharmacyDataSource by lazy {
        retrofit.create(PharmacyDataSource::class.java)
    }

}
