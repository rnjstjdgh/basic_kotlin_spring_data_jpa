package com.example.demo

import org.springframework.batch.item.file.mapping.DefaultLineMapper

// todo: kotlin 에서 이게 되는 방법을 잘 모르겠다...
//class CustomKotlinLineMapper<T>: DefaultLineMapper<T>() {
//
//    override fun mapLine(line: String, lineNumber: Int): T? {
//        if (line.startsWith("T")) {
//            return null
//        }
//        return super.mapLine(line, lineNumber)
//    }
//}