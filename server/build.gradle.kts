import org.jetbrains.kotlin.ir.backend.js.compile

val ktor_version = "1.3.2"

group = "Example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    java
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
}

application {
    mainClassName="MainKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    project(":SharedCode")
    implementation("ch.qos.logback:logback-classic:1.2.3")
}