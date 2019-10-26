package com.orangeinfinity.demo.repositories

import com.orangeinfinity.demo.model.entities.Donation
import org.springframework.data.jpa.repository.JpaRepository

interface DonationRepository : JpaRepository<Donation, String> {

    override fun getOne(id: String): Donation
}