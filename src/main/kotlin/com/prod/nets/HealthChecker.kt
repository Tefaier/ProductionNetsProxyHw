package com.prod.nets

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.stream.Collectors

@Component
class HealthChecker(@param:Value("#{'\${upstream.ips}'.split(',')}") private val upstreamIps: List<String>, private val restTemplate: RestTemplate) {
    private var healthy = upstreamIps.toList()

    fun getHealthy(): List<String> {
        return healthy
    }

    @Scheduled(fixedRate = 5000)
    fun checkConnection() {
        healthy = healthyUpstreams
    }

    private fun isUpstreamHealthy(upstream: String): Boolean {
        try {
            val response = restTemplate.getForEntity(
                "https://$upstream/ping", String::class.java
            )
            return response.statusCode.is2xxSuccessful
        } catch (e: Exception) {
            return false
        }
    }

    private val healthyUpstreams: List<String>
        get() = upstreamIps.stream().parallel()
            .filter { upstream: String -> this.isUpstreamHealthy(upstream) }
            .collect(Collectors.toList())
}