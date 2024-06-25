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
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    private const val PHARMACY_API_BASE_URL = "https://api.odcloud.kr/api/"
    private const val GEO_CODE_BASE_URL = "https://naveropenapi.apigw.ntruss.com/"
    @Provides
    fun provideAuthorizationInterceptor(): Interceptor {
        return AuthorizationInterceptor()
    }

    fun provideOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(authorizationInterceptor)
            .build()
    }

    @Provides
    @Named("PharmacyApi")
    fun providePharmacyApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PHARMACY_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Named("GeoCodeApi")
    fun provideGeoCodeApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GEO_CODE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providePharmacyApiService(@Named("PharmacyApi") retrofit: Retrofit): PharmacyDataSource {
        return retrofit.create(PharmacyDataSource::class.java)
    }

    @Provides
    fun provideGeoCodeApiService(@Named("GeoCodeApi") retrofit: Retrofit): GeoCodeDataSource {
        return retrofit.create(GeoCodeDataSource::class.java)
    }
}
