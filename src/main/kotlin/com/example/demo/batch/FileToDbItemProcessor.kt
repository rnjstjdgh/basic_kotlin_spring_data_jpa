package com.example.demo.batch

import com.example.demo.batch.entity.DBEntity
import com.example.demo.batch.entity.FileToDBDto
import mu.KotlinLogging
import org.springframework.batch.item.ItemProcessor


class FileToDbItemProcessor: ItemProcessor<FileToDBDto, DBEntity> {

    private val log = KotlinLogging.logger {}

    override fun process(item: FileToDBDto): DBEntity? {
        return item.let {
            DBEntity(
                partnerCode = it.partnerCode,
                managementNumber = it.managementNumber,
                sequenceNumber = it.sequenceNumber
            )
        }
    }
}