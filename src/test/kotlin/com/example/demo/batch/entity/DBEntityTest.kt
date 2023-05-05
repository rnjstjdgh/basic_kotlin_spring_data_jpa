package com.example.demo.batch.entity

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.*

@DataJpaTest
internal class DBEntityTest @Autowired constructor(
    private val em: EntityManager
){

    @Test
    fun `jpa persist, merge 테스트`() {

        em.persist(
            TestDBEntity(
                id = 1,
                partnerCode = "partnerCode1",
            )
        )

        em.merge(
            TestDBEntity(
                id = 1,
                partnerCode = "partnerCode2",
            )
        )
    }
}

@Entity
@Table(name = "TEST_DB_ENTITY")
class TestDBEntity(

    @Id
    val id: Long = 0,

    @Column(name = "PARTNER_CODE")
    val partnerCode: String,
)