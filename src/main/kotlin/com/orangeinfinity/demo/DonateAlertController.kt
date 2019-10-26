package com.orangeinfinity.demo

import com.orangeinfinity.demo.model.invoice.InvoiceCreateDto
import com.orangeinfinity.demo.model.invoice.InvoiceResultDto
import com.orangeinfinity.demo.model.invoice.LinkDto
import org.apache.commons.logging.LogFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/"])
class DonateAlertController(val plasmaApiService: PlasmaApiService) {

    private val log = LogFactory.getLog(javaClass)
    private val apiKey = "d16350f8-2597-42bf-b72a-a3baaeff11e3"

    @CrossOrigin(origins = ["*"])
    @PostMapping("/newDonation/confirm")
    fun createInvoice(@RequestBody requestJson: String) {
        log.info("Received webhook info: $requestJson")

        println(4)
    }

    @CrossOrigin(origins = ["*"])
    @PostMapping("newDonation/get")
    fun getNewDonation(@RequestBody requestJson: String): String? {
        val invoiceDto = JacksonUtils.serializingObjectMapper().readValue(requestJson, InvoiceCreateDto::class.java)
        val invoiceResponse = plasmaApiService.createInvoice(
                apiKey,
                invoiceDto.amount,
                invoiceDto.currencyCode,
                invoiceDto.comment
        )
        log.info("Creating invoice ended. Result: $invoiceResponse")
        val invoiceResultDto = JacksonUtils.serializingObjectMapper()
                .readValue(invoiceResponse, InvoiceResultDto::class.java)
        val linkDto = LinkDto(invoiceResultDto.link)

        return JacksonUtils.serializingObjectMapper().writeValueAsString(linkDto)
    }
}