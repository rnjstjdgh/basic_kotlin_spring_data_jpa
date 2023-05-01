package com.example.demo

import com.example.demo.domain.User
import com.example.demo.domain.Worker
import com.example.demo.domain.context.EntityContext
import com.example.demo.domain.port.out.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

@RestController
@RequestMapping("/test")
class TestController(
        private val userRepository: UserRepository
) {

    @GetMapping("/health-check")
    fun healthCheck(): ResponseEntity<String> {
        return ResponseEntity.ok("health-check")
    }

    @GetMapping("user/save")
    fun saveUser(
            @RequestParam name: String,
            @RequestParam requestUniqueId: String
    ): ResponseEntity<User> {

        EntityContext.threadLocalWorker.set(Worker(
                uniqueId = requestUniqueId,
                time = LocalDateTime.now()
        ))

        val transientUser = User(name = "name")
        val persistUser = userRepository.save(transientUser)
        return ResponseEntity.ok(persistUser)
    }

    @GetMapping("user/update/{user_id}")
    fun updateUser(
            @PathVariable("user_id") id: Long,
            @RequestParam name: String,
            @RequestParam requestUniqueId: String
    ): ResponseEntity<User> {

        EntityContext.threadLocalWorker.set(Worker(
                uniqueId = requestUniqueId,
                time = LocalDateTime.now()
        ))

        val findUser = userRepository.findByIdOrNull(id)
                ?: throw IllegalArgumentException("IllegalArgumentException")
        findUser.modify(name = name)
        val modifyUser = userRepository.save(findUser)
        return ResponseEntity.ok(modifyUser)
    }
}