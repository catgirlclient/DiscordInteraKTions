package live.shuuyu.scripts

import gradle.kotlin.dsl.accessors._6dfd0906a4509b327c85862d9682362c.javadoc
import gradle.kotlin.dsl.accessors._6dfd0906a4509b327c85862d9682362c.kotlinSourcesJar
import live.shuuyu.scripts.utils.Project

plugins {
    `maven-publish`
    signing
}

publishing {
    publications {
        withType<MavenPublication>().configureEach {
            artifactId = project.name
            groupId = Project.GROUP
            version = Project.VERSION

            pom {
                name = "Discord InteraKTions"
                description = "Kotlin Library for Receiving and Handling Discord Interactions via Web Servers/Gateway with Kord"
                url = Project.URL
                packaging = "jar"

                scm {
                    connection.set("scm:git:https://github.com/catgirlclient/DiscordInteraKTions.git")
                    developerConnection.set("scm:git:https://github.com/catgirlclient/DiscordInteraKTions.git")
                    url.set(Project.URL)
                }

                licenses {
                    license {
                        name.set("GNU Lesser General Public License Version 3.0")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.en.html")
                    }
                }

                developers {
                    developer {
                        name = "yujin"
                    }
                }

                issueManagement {
                    system = "GitHub"
                    url = "https://github.com/catgirlclient/DiscordInteraKTions/issues"
                }
            }
        }

        register(Project.NAME, MavenPublication::class.java) {
            from(components["java"])
            artifact(tasks.javadoc)
            artifact(tasks.kotlinSourcesJar)
        }
    }

    repositories {
        maven {
            name = "Sonatype"
            url = if(Project.VERSION.endsWith("-SNAPSHOT")) {
                uri(Project.SONATYPE_SNAPSHOT)
            } else { uri(Project.SONATYPE_RELEASE) }
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

signing {
    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}