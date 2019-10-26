package com.orangeinfinity.demo.model.invoice

class InvoiceCreateDto(
        val amount: Double,
        val currencyCode: String,
        val nickname: String,
        val comment: String
)