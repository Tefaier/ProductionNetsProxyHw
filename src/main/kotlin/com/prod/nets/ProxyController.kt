package com.prod.nets

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class ProxyController(private val proxyService: RoundRobinProxy) {
    @RequestMapping(value = ["/**"], method = [RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.HEAD, RequestMethod.TRACE, RequestMethod.OPTIONS])
    fun allProxy(
        @RequestBody(required = false) body: String?,
        request: HttpServletRequest
    ): ResponseEntity<*> {
        val method = HttpMethod.valueOf(request.method)

        try {
            val (code, result) = proxyService.forwardRequest(
                request.scheme,
                request.requestURI,
                String::class.java, method, body, extractHeaders(request)
            )
            return ResponseEntity.status(code).body(result)
        } catch (e: Exception) {
            return ResponseEntity.status(500).body("Proxy error: " + e.message)
        }
    }

    fun extractHeaders(request: HttpServletRequest): HttpHeaders {
        val headers = HttpHeaders()

        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val headerName = headerNames.nextElement()
            val headerValues = request.getHeaders(headerName)
            while (headerValues.hasMoreElements()) {
                val headerValue = headerValues.nextElement()
                headers.add(headerName, headerValue)
            }
        }

        return headers
    }
}