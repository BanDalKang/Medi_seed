package com.mediseed.mediseed.network.di

import com.mediseed.mediseed.data.remote.GeoCodeDataSource
import com.mediseed.mediseed.data.remote.PharmacyDataSource
import com.mediseed.mediseed.network.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val PHARMACY_API_BASE_URL = "https://api.odcloud.kr/api/"
    private const val GEO_CODE_BASE_URL = "https://naveropenapi.apigw.ntruss.com/"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PharmacyApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GeoCodeApi

    @Provides
    fun provideAuthorizationInterceptor(): Interceptor {
        return AuthorizationInterceptor()
    }

    @Provides
    fun provideOkHttpClient(
        authorizationInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(authorizationInterceptor)
            .build()
    }

    @Provides
    @PharmacyApi
    @Singleton
    fun providePharmacyApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PHARMACY_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @GeoCodeApi
    @Singleton
    fun provideGeoCodeApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GEO_CODE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePharmacyApiService(@PharmacyApi retrofit: Retrofit): PharmacyDataSource {
        return retrofit.create(PharmacyDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideGeoCodeApiService(@GeoCodeApi retrofit: Retrofit): GeoCodeDataSource {
        return retrofit.create(GeoCodeDataSource::class.java)
    }
}