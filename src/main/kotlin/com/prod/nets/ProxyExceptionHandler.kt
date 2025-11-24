package com.prod.nets

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice(assignableTypes = [ProxyController::class])
class ProxyExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleProxyException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity.status(502)
            .body("Proxy service unavailable: " + ex.message)
    }
}