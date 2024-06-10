package com.mediseed.mediseed.domain.exception

class QuotaExceededException(
    override val message: String?
) : RuntimeException()