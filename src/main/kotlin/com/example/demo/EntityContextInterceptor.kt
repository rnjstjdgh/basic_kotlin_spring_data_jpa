package com.example.demo

import com.example.demo.domain.Worker
import com.example.demo.domain.context.EntityContext
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class EntityContextInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        EntityContext.threadLocalWorker.set(Worker(
                uniqueId = request.getParameter("requestUniqueId"),
                time = LocalDateTime.now()
        ))
        return true
    }
}