import live.shuuyu.scripts.utils.Project
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import java.time.Year

plugins {
    org.jetbrains.dokka
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:2.0.0")
    }
}

group = Project.GROUP
version = Project.VERSION

allprojects {
    tasks.withType<DokkaMultiModuleTask> {
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "© ${Year.now().value} Shuuyu"
        }
    }
}

