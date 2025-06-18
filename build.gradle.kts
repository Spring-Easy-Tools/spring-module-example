plugins {
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    val kotlinVersion = "2.1.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
}

group = "ru.virgil.spring"
version = "0.0.1-SNAPSHOT"

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

repositories {
    mavenCentral()
}

dependencies {

    // Модуль инструментов Spring
    implementation("ru.virgil.spring:spring-module-tools")

    // Зависимости Spring
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.security:spring-security-messaging")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.session:spring-session-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.security:spring-security-test")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    // Third-party зависимости
    implementation("net.datafaker:datafaker:1.9.0")
    implementation("org.zalando:logbook-spring-boot-starter:3.12.1")
    implementation("io.mikael:urlbuilder:2.0.9")
    implementation("com.google.api-client:google-api-client:2.4.0")
    implementation("org.instancio:instancio-core:5.4.1")

    // Зависимости для тестирования
    testImplementation("org.awaitility:awaitility:4.2.1")
    testImplementation("org.awaitility:awaitility-kotlin:4.2.1")

    // Зависимости для разработки
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

kotlin {
    @Suppress("SpellCheckingInspection")
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

/**
 * Пришлось добавить этот костыль, чтобы не вылазила ошибка snakeyaml android
 * https://github.com/DiUS/java-faker/issues/327#issuecomment-1094277568
 */
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.module.toString() == "org.yaml:snakeyaml") {
            artifactSelection {
                selectArtifact(DependencyArtifact.DEFAULT_TYPE, null, null)
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
