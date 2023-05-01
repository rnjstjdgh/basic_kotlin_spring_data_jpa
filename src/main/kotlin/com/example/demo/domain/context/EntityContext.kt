package com.example.demo.domain.context

import com.example.demo.domain.Worker

class EntityContext {
    companion object {
        val threadLocalWorker = ThreadLocal<Worker>()
    }
}