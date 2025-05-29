plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "me.maximilian-milz"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")

    // Database dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Custom Docker tasks
tasks.register<Exec>("dockerComposeUp") {
    group = "docker"
    description = "Starts Docker containers using docker-compose"

    commandLine("docker-compose", "up", "-d")

    // Wait for the database to be ready
    doLast {
        println("Docker containers started. Waiting for database to be ready...")
        var ready = false
        var attempts = 0
        val maxAttempts = 10

        while (!ready && attempts < maxAttempts) {
            try {
                val process = ProcessBuilder("docker-compose", "exec", "postgres", "pg_isready", "-U", "skeleton_user", "-d", "skeleton_db")
                    .redirectErrorStream(true)
                    .start()

                val exitCode = process.waitFor()
                ready = exitCode == 0

                if (!ready) {
                    println("Database not ready yet. Waiting...")
                    Thread.sleep(2000) // Wait 2 seconds before trying again
                    attempts++
                }
            } catch (e: Exception) {
                println("Error checking database readiness: ${e.message}")
                attempts++
                Thread.sleep(2000)
            }
        }

        if (ready) {
            println("Database is ready!")
        } else {
            println("Warning: Database might not be ready after $maxAttempts attempts")
        }
    }
}

tasks.register<Exec>("dockerComposeDown") {
    group = "docker"
    description = "Stops Docker containers using docker-compose"

    commandLine("docker-compose", "down")

    doLast {
        println("Docker containers stopped")
    }
}

// Make bootRun depend on dockerComposeUp to start Docker containers before the application
tasks.named("bootRun") {
    dependsOn("dockerComposeUp")

    doFirst {
        println("Starting application with Docker containers...")
    }

    doLast {
        println("Application started. To stop Docker containers when done, run './gradlew dockerComposeDown'")
    }
}

// Add a task to stop containers when the application stops
tasks.register("stopDockerContainers") {
    group = "application"
    description = "Stops Docker containers"

    dependsOn("dockerComposeDown")
}
