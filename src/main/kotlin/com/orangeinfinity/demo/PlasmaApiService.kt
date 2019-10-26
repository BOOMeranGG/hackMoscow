package com.orangeinfinity.demo

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Component
class PlasmaApiService {

    private val createInvoiceEndpoint = "https://app.plasmapay.com/business/api/v1/public/invoices"
    private var restTemplate: RestTemplate = RestTemplate()

    fun createInvoice(apiKey: String, amount: Double, currencyCode: String, comment: String): String? {
        val header = HttpHeaders()
        val builder = UriComponentsBuilder.fromHttpUrl(createInvoiceEndpoint)
                .queryParam("apiKey", apiKey)
                .queryParam("currencyCode", "RUBp")
                .queryParam("amount", amount)
                .queryParam("merchantId", UUID.randomUUID().toString())
                .queryParam("description", "donation")
                .queryParam("accountId", UUID.randomUUID().toString())
        val httpEntity = HttpEntity<HttpHeaders>(header)

        val response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                httpEntity,
                String::class.java
        )
        return response.body
    }
}