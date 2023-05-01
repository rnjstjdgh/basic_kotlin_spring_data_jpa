package com.example.demo.domain

import java.time.LocalDateTime
import javax.persistence.Embeddable

@Embeddable
data class Worker(
        val uniqueId: String,
        val time: LocalDateTime
)