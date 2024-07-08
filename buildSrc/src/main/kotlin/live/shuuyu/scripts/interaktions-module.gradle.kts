package live.shuuyu.scripts

import kotlinx.validation.ExperimentalBCVApi
import live.shuuyu.scripts.utils.Project
import org.jetbrains.dokka.gradle.AbstractDokkaLeafTask
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

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
        apiVersion.set(KotlinVersion.KOTLIN_2_0)
        languageVersion.set(KotlinVersion.KOTLIN_2_0)
        progressiveMode = true
        freeCompilerArgs.add("-Xdont-warn-on-error-suppression")
    }
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }

    withType<AbstractDokkaLeafTask>().configureEach {
        moduleName = project.name

        failOnWarning = true

        dokkaSourceSets.configureEach {
            jdkVersion.set(17)
            suppressGeneratedFiles = true

            externalDocumentationLink("https://kotlinlang.org/")
        }
    }
}

publishing {
    publications.register<MavenPublication>(Project.NAME) {
        from(components["java"])
        artifact(tasks.javadoc)
        artifact(tasks.kotlinSourcesJar)
    }
}

@OptIn(ExperimentalBCVApi::class)
apiValidation {
    klib.enabled = true
}