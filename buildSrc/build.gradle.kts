plugins {
    `kotlin-dsl`
    kotlin("jvm") version "2.2.0"
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.kikugie.dev/releases")
}

dependencies {
    implementation("dev.kikugie:stonecutter:0.7.7")
}