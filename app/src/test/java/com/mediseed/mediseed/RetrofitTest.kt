package com.mediseed.mediseed

import com.mediseed.mediseed.network.di.RetrofitClient
import com.mediseed.mediseed.repository.GeoCodeRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RetrofitTest {

    @Test
    fun retrofit_test () = runBlocking {
        val pharmacyRepository = GeoCodeRepositoryImpl(RetrofitClient.geoCodeDataSource)
        println( pharmacyRepository.getGeoCode("서울 종로구 신문로1가 115"))

        }

    }
