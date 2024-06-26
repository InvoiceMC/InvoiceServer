plugins {
    kotlin("jvm") version "2.0.0-RC1"
    `maven-publish`
}

group = "org.github.invoicemc"
version = "0.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    api("net.minestom:minestom-snapshots:7320437640")
    api("net.kyori:adventure-text-minimessage:4.16.0")
    api("com.google.code.gson:gson:2.10.1")

    api("org.slf4j:slf4j-api:2.0.9")
    api("ch.qos.logback:logback-classic:1.5.6")

    implementation("cc.ekblad:4koma:1.2.0")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "8.5"
    distributionType = Wrapper.DistributionType.ALL
}

java {
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    jvmToolchain(21)
}