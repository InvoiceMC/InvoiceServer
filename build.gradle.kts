plugins {
    kotlin("jvm") version "2.0.0-RC1"
}

group = "net.invoice"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("net.minestom:minestom-snapshots:7320437640")
}

kotlin {
    jvmToolchain(21)
}