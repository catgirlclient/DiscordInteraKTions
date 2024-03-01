import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.AbstractDokkaTask
import java.time.Year

plugins {
    org.jetbrains.dokka
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.9.10")
    }
}

val discordInteraKTionsVersion = "0.1.0"
group = "net.perfectdreams.discordinteraktions"
version = discordInteraKTionsVersion

allprojects {
    tasks.withType<AbstractDokkaTask> {
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "© ${Year.now().value} Shuuyu"
        }
    }
}

