package com.orangeinfinity.demo.model.invoice

import java.util.*

class InvoiceResultDto {
    lateinit var invoice: InvoiceDto
}


class InvoiceDto {
    var amount: Double = 0.0
    lateinit var currencyCode: String
    lateinit var status: String
    lateinit var extId: String
    lateinit var createdAt: Date
    lateinit var link: String
}
