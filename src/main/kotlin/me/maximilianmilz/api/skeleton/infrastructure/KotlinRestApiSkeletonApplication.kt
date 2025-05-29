package me.maximilianmilz.api.skeleton.infrastructure

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["me.maximilianmilz.api.skeleton"])
class KotlinRestApiSkeletonApplication

fun main(args: Array<String>) {
    runApplication<KotlinRestApiSkeletonApplication>(*args)
}