package me.maximilianmilz.api.skeleton.infrastructure.config

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.util.concurrent.Executors

/**
 * Configuration for Docker containers.
 * This class is responsible for starting the Docker containers when the application starts up.
 */
@Configuration
class DockerConfig {

    @Bean
    fun dockerRunner(): ApplicationRunner {
        return ApplicationRunner {
            val executor = Executors.newSingleThreadExecutor()
            executor.submit {
                try {
                    // Check if Docker is installed
                    val dockerCheckProcess = ProcessBuilder("docker", "--version")
                        .redirectErrorStream(true)
                        .start()
                    
                    dockerCheckProcess.inputStream.bufferedReader().use { reader ->
                        val output = reader.readText()
                        if (!output.contains("Docker version")) {
                            println("Docker is not installed or not in PATH. Please install Docker to use the database container.")
                            return@submit
                        }
                    }
                    
                    // Start Docker container using docker-compose
                    val dockerComposeFile = File("docker-compose.yml")
                    if (!dockerComposeFile.exists()) {
                        println("docker-compose.yml not found in the current directory.")
                        return@submit
                    }
                    
                    println("Starting PostgreSQL container...")
                    val process = ProcessBuilder("docker-compose", "up", "-d")
                        .redirectErrorStream(true)
                        .start()
                    
                    process.inputStream.bufferedReader().use { reader ->
                        val output = reader.readText()
                        println("Docker Compose output: $output")
                    }
                    
                    val exitCode = process.waitFor()
                    if (exitCode == 0) {
                        println("PostgreSQL container started successfully.")
                    } else {
                        println("Failed to start PostgreSQL container. Exit code: $exitCode")
                    }
                } catch (e: Exception) {
                    println("Error starting Docker container: ${e.message}")
                    e.printStackTrace()
                }
            }
            executor.shutdown()
        }
    }
}