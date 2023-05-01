package com.example.demo.domain

import com.example.demo.domain.context.EntityContext
import mu.KotlinLogging
import javax.persistence.*

val log = KotlinLogging.logger {}

/***
 * 요기에 들어갈 녀석들을 완전히 비즈니스와 무관한 놈들이라고 간주한다면 이러한 설계가 이득이 있을듯?!
 */
@MappedSuperclass
open class BaseEntity(

        @AttributeOverrides(
                AttributeOverride(name = "uniqueId", column = Column(name = "CREATE_UUID")),
                AttributeOverride(name = "time", column = Column(name = "CREATE_TIME"))
        )
        private var createWorker: Worker? = null,

        @AttributeOverrides(
                AttributeOverride(name = "uniqueId", column = Column(name = "UPDATE_UUID")),
                AttributeOverride(name = "time", column = Column(name = "UPDATE_TIME"))
        )
        private var updateWorker: Worker? = null
) {

    @PrePersist
    private fun prePersist() {
        log.info { "<============prePersist 실행!============>" }
        val workerContext = EntityContext.threadLocalWorker.get()   // todo <- 밖에서 세팅 못해줬다면? 부가정보로 간주하고 그냥 null 로?
        createWorker = workerContext
        updateWorker = workerContext
        log.info { "<============prePersist 종료!============>" }
    }

    @PreUpdate
    private fun preUpdate() {
        log.info { "<============preUpdate 실행!============>" }
        val workerContext = EntityContext.threadLocalWorker.get()
        updateWorker = workerContext
        log.info { "<============preUpdate 종료!============>" }
    }
}