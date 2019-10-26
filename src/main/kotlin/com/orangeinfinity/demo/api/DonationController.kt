package com.orangeinfinity.demo.api

import com.orangeinfinity.demo.model.entities.Donation
import com.orangeinfinity.demo.model.invoice.InvoiceCreateDto
import com.orangeinfinity.demo.model.invoice.InvoiceResultDto
import com.orangeinfinity.demo.model.invoice.LinkDto
import com.orangeinfinity.demo.repositories.DonationRepository
import com.orangeinfinity.demo.utils.JacksonUtils
import org.apache.commons.logging.LogFactory
import org.springframework.web.bind.annotation.*


private const val SUCCESS_CODE = "success"

@RestController
@RequestMapping(value = ["/api/v1/"])
class DonationController(
        val plasmaApiService: PlasmaApiService,
        val donationRepository: DonationRepository
) {
    private val log = LogFactory.getLog(javaClass)
    private val apiKey = "d16350f8-2597-42bf-b72a-a3baaeff11e3"

    @CrossOrigin(origins = ["*"])
    @PostMapping("newDonation/create")
    fun createInvoice(@RequestBody requestJson: String): String? {
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
        val donation = buildNotPaidDonation(invoiceResultDto, invoiceDto.nickname, invoiceDto.comment)
        donationRepository.save(donation)

        return JacksonUtils.serializingObjectMapper().writeValueAsString(linkDto)
    }

    @CrossOrigin(origins = ["*"])
    @PostMapping("/newDonation/confirm")
    fun updatePayedInvoice(@RequestBody requestJson: String) {
        log.info("Received webhook info: $requestJson")
        val invoiceResult = JacksonUtils.serializingObjectMapper().readValue(requestJson, InvoiceResultDto::class.java)

        if (invoiceResult.status == SUCCESS_CODE) {
            val donation = donationRepository.getOne(invoiceResult.id)
            donation.link = invoiceResult.link
            donation.isFee = true
            donation.payedDate = invoiceResult.createdAt
            donationRepository.save(donation)
        }
    }

    @CrossOrigin(origins = ["*"])
    @PostMapping("/newDonation/get")
    fun getNextDonation(): String {
        //WARNING! AHTUNG! BIDLOCODE
        val donations = donationRepository.findAll()
        val notShowedDonations = donations.filter {
            it.isFee && !it.visited
        }
        if (notShowedDonations.isNullOrEmpty()) {
            return "{}"
        }

        val sortedNotShowedDonations = notShowedDonations.sortedBy { it.payedDate }
        val nextDonation = sortedNotShowedDonations.first()
        nextDonation.visited = true
        donationRepository.save(nextDonation)

        return JacksonUtils.serializingObjectMapper().writeValueAsString(nextDonation)
    }

    private fun buildNotPaidDonation(invoice: InvoiceResultDto, nickname: String, comment: String): Donation {
        val donation = Donation()
        donation.id = invoice.id
        donation.nickname = nickname
        donation.currencyCode = invoice.currencyCode
        donation.comment = comment
        donation.amount = invoice.amount
        donation.link = invoice.link

        return donation
    }
}