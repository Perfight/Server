plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.sychartik"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val exposedVersion: String by project
dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-network:2.3.12")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-money:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")
    implementation("org.postgresql:postgresql:42.2.23")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}