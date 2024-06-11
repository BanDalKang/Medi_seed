package com.mediseed.mediseed

import com.mediseed.mediseed.network.PharmacyRetrofitClient
import com.mediseed.mediseed.repository.PharmacyRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RetrofitTest {

    @Test
    fun retrofit_test () = runBlocking {
        val pharmacyRepository = PharmacyRepositoryImpl(PharmacyRetrofitClient.pharmacyDataSource)
        pharmacyRepository.getPharmacyDaegeonSeogu().data.forEach {
            println(it)
        }

    }
}