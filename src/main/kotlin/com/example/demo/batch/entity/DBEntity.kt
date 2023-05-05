package com.example.demo.batch.entity

import javax.persistence.*

@Entity
@Table(name = "DB_ENTITY")
class DBEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "PARTNER_CODE")
    val partnerCode: String,

    @Column(name = "MGMT_NO")
    val managementNumber: String,

    @Column(name = "SEQ_NO")
    val sequenceNumber: String
)