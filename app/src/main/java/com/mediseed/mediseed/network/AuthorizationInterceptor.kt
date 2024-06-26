package com.mediseed.mediseed.network

import com.mediseed.mediseed.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

//Interceptor: 네트워크 요청 및 응답 가로채 원하는 작업 수행(네트워크 작업 로깅) / 모든 요청에 동일한 헤더 추가(중앙집중식 관리)
 class AuthorizationInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest = chain.request().newBuilder()
                .addHeader("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_MAP_CLIENT_ID)
                .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_MAP_CLIENT_SECRET_ID)
                .build()

            return chain.proceed(newRequest)
        }
    }
