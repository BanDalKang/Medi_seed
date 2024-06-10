package com.mediseed.mediseed.domain.exception

class UnknownHttpException(
    val code: Int?,
    override val message: String?
) : RuntimeException()