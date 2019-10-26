package com.orangeinfinity.demo.model.invoice

import java.util.*

class InvoiceResultDto(
        var id: String,
        val amount: Double,
        val currencyCode: String,
        val status: String,
        val createdAt: Date,
        val link: String
)