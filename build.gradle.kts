import live.shuuyu.scripts.utils.Project
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.AbstractDokkaTask
import java.time.Year

plugins {
    org.jetbrains.dokka
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.9.20")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}

group = Project.GROUP
version = Project.VERSION

allprojects {
    tasks.withType<AbstractDokkaTask> {
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "© ${Year.now().value} Shuuyu"
        }
    }
}

