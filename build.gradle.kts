import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10" apply false
    kotlin("plugin.serialization") version "1.8.10" apply false
    `maven-publish`
}

val discordInteraKTionsVersion = "0.0.17"
group = "net.perfectdreams.discordinteraktions"
version = discordInteraKTionsVersion

repositories {
    mavenCentral()
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.perfectdreams.net")
    }
}

subprojects {
    apply<MavenPublishPlugin>()
    version = discordInteraKTionsVersion

    publishing {
        repositories {
            maven {
                name = "PerfectDreams"
                url = uri("https://repo.perfectdreams.net/")
                credentials(PasswordCredentials::class)
            }
        }
    }
}

