package com.example.demo.domain.port.out

import com.example.demo.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>