package live.shuuyu.scripts

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
                        name = "shuuyu"
                    }
                }

                issueManagement {
                    system = "GitHub"
                    url = "https://github.com/catgirlclient/DiscordInteraKTions/issues"
                }
            }
        }
    }

    repositories {
        maven {
            name = "Reposilite"
            url = uri("https://maven.shuyu.me")
            credentials {
                username = System.getenv("MAVEN_USERNAME") ?: rootProject.findProject("MAVEN_USERNAME").toString()
                password = System.getenv("MAVEN_PASSWORD") ?: rootProject.findProject("MAVEN_PASSWORD").toString()
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}