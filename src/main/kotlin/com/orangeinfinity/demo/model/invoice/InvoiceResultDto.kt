package com.orangeinfinity.demo.model.invoice

class InvoiceResultDto(
        val id: String,
        val amount: Double,
        val currencyCode: String,
        val status: String,
        val createdAt: String,
        val link: String
)