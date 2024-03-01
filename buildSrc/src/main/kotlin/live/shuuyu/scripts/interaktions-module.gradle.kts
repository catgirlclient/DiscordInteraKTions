package live.shuuyu.scripts

import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
import live.shuuyu.scripts.utils.Project

plugins {
    org.jetbrains.kotlin.jvm
    org.jetbrains.kotlin.plugin.serialization
    org.jetbrains.dokka
    org.jetbrains.kotlinx.`binary-compatibility-validator`
    `maven-publish`
    signing
}

group = Project.GROUP
version = Project.VERSION

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

kotlin {
    explicitApi()
    jvmToolchain(17) // Minimum (LTS) Java version we support

    compilerOptions {
        progressiveMode = true
    }
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }

    withType<AbstractDokkaLeafTask> {
        moduleName = project.name
        failOnWarning = true

        dokkaSourceSets.configureEach {
            jdkVersion.set(17)
            suppressGeneratedFiles = true

            sourceLink {
                localDirectory.set(projectDir)
            }

            externalDocumentationLink("https://kotlinlang.org/")
        }
    }
}