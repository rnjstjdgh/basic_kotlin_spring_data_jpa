package com.example.demo.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "USER_TABLE")
class User(

        @Id
        @GeneratedValue
        val id: Long = 0,

        @Column(name = "NAME")
        var name: String

) : BaseEntity() {

    fun modify(name: String? = null) {
        name?.let { this.name = it }
    }
}