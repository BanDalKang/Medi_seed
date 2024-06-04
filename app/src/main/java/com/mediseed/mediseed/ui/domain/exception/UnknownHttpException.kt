package com.mediseed.mediseed.ui.domain.exception

class UnknownHttpException(
    val code: Int?,
    override val message: String?
) : RuntimeException()