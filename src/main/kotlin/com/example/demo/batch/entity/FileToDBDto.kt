package com.example.demo.batch.entity

data class FileToDBDto(
    var partnerCode: String = DEFAULT_STRING,    // todo val 로 하면 BeanWrapperFieldSetMapper#mapFieldSet > DataBinder#bind 에서 값 세팅 못함
    var managementNumber: String = DEFAULT_STRING,
    var sequenceNumber: String = DEFAULT_STRING
) {

    companion object {
        private const val DEFAULT_STRING = "default"
    }
}