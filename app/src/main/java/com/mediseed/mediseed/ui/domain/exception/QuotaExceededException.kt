package com.mediseed.mediseed.ui.domain.exception

class QuotaExceededException(
    override val message: String?
) : RuntimeException()