package com.mediseed.mediseed

import com.mediseed.mediseed.ui.data.network.PharmacyRetrofitClient
import com.mediseed.mediseed.ui.data.repository.PharmacyRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RetrofitTest {

    @Test
    fun retrofit_test () = runBlocking {
        val pharmacyRepository = PharmacyRepositoryImpl(PharmacyRetrofitClient.pharmacyDataSource)
        pharmacyRepository.getPharmacy().data.forEach {
            println(it)
        }

    }
}