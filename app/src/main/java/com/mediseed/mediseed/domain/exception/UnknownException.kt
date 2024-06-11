package com.mediseed.mediseed.domain.exception

class UnknownException(
    override val message: String?
) : RuntimeException()