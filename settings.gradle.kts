@file:Suppress("UnstableApiUsage")

pluginManagement {
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

rootProject.name = "DiscordInteraKTions"

include(
    ":sample",
    ":common",
    ":requests-verifier",
    ":platforms:gateway-kord",
    ":platforms:webserver-ktor-kord",
)