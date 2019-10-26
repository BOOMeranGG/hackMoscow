package com.orangeinfinity.demo.model.entities

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "donation")
class Donation {

    @Id
    @Column(name = "ID")
    var id = ""

    @Column(name = "NICKNAME")
    lateinit var nickname: String

    @Column(name = "CURRENCY_CODE")
    lateinit var currencyCode: String

    @Column(name = "COMMENT")
    @Lob
    lateinit var comment: String

    @Column(name = "AMOUNT")
    var amount: Double = 0.0

    @Column(name = "IS_FEE")
    var isFee = false

    @Column(name = "LINK")
    lateinit var link: String

    @Column(name = "PAYED_TIME")
    lateinit var payedDate: Date
}