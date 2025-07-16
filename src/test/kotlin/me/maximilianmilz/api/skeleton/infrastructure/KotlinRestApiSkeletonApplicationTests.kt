package me.maximilianmilz.api.skeleton.infrastructure

import me.maximilianmilz.api.skeleton.infrastructure.config.TestContainersConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class KotlinRestApiSkeletonApplicationTests : TestContainersConfig() {

    @Test
    fun contextLoads() {
    }

}
