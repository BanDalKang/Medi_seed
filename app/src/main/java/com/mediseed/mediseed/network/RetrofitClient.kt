package com.mediseed.mediseed.network

import com.mediseed.mediseed.data.remote.GeoCodeDataSource
import com.mediseed.mediseed.data.remote.PharmacyDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val PHARMACY_API_BASE_URL = "https://api.odcloud.kr/api/"
    private const val GEO_CODE_BASE_URL = "https://naveropenapi.apigw.ntruss.com/"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    private val pharmacyRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(PHARMACY_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val getCodeRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(GEO_CODE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pharmacyDataSource: PharmacyDataSource by lazy {
        pharmacyRetrofit.create(PharmacyDataSource::class.java)
    }

    val geoCodeDataSource: GeoCodeDataSource by lazy {
        getCodeRetrofit.create(GeoCodeDataSource::class.java)
    }
}