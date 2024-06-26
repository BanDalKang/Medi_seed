package com.mediseed.mediseed.repository

import com.mediseed.mediseed.data.model.DaejeonSeoguResponse
import com.mediseed.mediseed.data.model.DaejeonYuseongguResponse
import com.mediseed.mediseed.data.model.GeoCodeResponse
import com.mediseed.mediseed.data.remote.GeoCodeDataSource
import com.mediseed.mediseed.data.remote.PharmacyDataSource
import com.mediseed.mediseed.domain.exception.NetworkException
import com.mediseed.mediseed.domain.exception.QuotaExceededException
import com.mediseed.mediseed.domain.exception.TimeoutException
import com.mediseed.mediseed.domain.exception.UnknownException
import com.mediseed.mediseed.domain.exception.UnknownHttpException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PharmacyRepositoryImpl @Inject constructor(
    private val pharmacyDataSource: PharmacyDataSource
) : PharmacyRepository {

    override suspend fun getDaejeonSeogu(): DaejeonSeoguResponse {
        return safeApiCall { pharmacyDataSource.getPharmacyDaejeonSeogu() }
    }

    override suspend fun getDaejeonYuseonggu(): DaejeonYuseongguResponse {
        return safeApiCall { pharmacyDataSource.getDaejeonYuseonggu() }
    }
}

@Singleton
class GeoCodeRepositoryImpl @Inject constructor(
     private val geoCodeDataSource: GeoCodeDataSource
) : GeoCodeRepository {
    override suspend fun getGeoCode(address: String?): GeoCodeResponse {
       return safeApiCall { geoCodeDataSource.getGeoCode(address) }
    }
}

private suspend fun <T> safeApiCall(apiCall: suspend () -> T): T {
    return try {
        apiCall()
    } catch (e: HttpException) {
        val message = e.message
        throw when (val code = e.code()) {
            403 -> QuotaExceededException(message)
            else -> UnknownHttpException(code, message)
        }
    } catch (e: SocketTimeoutException) {
        throw TimeoutException(e.message)
    } catch (e: UnknownHostException) {
        throw NetworkException(e.message)
    } catch (e: Exception) {
        throw UnknownException(e.message)
    }
}