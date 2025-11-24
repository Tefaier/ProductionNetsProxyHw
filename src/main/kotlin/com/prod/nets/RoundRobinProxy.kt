package com.prod.nets

import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.concurrent.atomic.AtomicInteger


@Component
class RoundRobinProxy(private val healthChecker: HealthChecker, private val restTemplate: RestTemplate) {
    private val currentIndex = AtomicInteger(0)

    fun <T> forwardRequest(
        scheme: String,
        path: String, responseType: Class<T>,
        method: HttpMethod, body: Any?,
        originalHeaders: HttpHeaders
    ): Pair<HttpStatusCode, T?> {
        val upstreamIp = nextUpstream
        if (nextUpstream == null) {
            return HttpStatusCode.valueOf(503) to null
        }
        val upstreamUrl = "$scheme$upstreamIp$path"

        originalHeaders.contentType = MediaType.APPLICATION_JSON
        originalHeaders.accept = listOf(MediaType.APPLICATION_JSON)

        val entity: HttpEntity<Any> = HttpEntity(body, originalHeaders)

        val response = restTemplate.exchange(
            upstreamUrl, method, entity, responseType
        )

        return response.statusCode to response.body
    }

    private val nextUpstream: String?
        get() {
            val upstream = healthChecker.getHealthy()
            if (upstream.isEmpty()) {
                return null
            }
            val index = currentIndex.getAndUpdate { i: Int -> (i + 1) % upstream.size }
            return upstream[index]
        }
}
