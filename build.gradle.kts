plugins {
    kotlin("jvm") version "1.9.10" apply false
    kotlin("plugin.serialization") version "1.9.10" apply false
    id("org.jetbrains.dokka") version "1.9.0"
    `maven-publish`
}

val discordInteraKTionsVersion = "0.1.0"
group = "net.perfectdreams.discordinteraktions"
version = discordInteraKTionsVersion

repositories {
    mavenCentral()
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
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

