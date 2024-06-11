package com.mediseed.mediseed.domain.exception

class TimeoutException(
    override val message: String?
) : RuntimeException()