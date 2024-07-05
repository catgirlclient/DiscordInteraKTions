package live.shuuyu.scripts

import live.shuuyu.scripts.utils.Project

plugins {
    `maven-publish`
    signing
}

publishing {
    publications {
        withType<MavenPublication>().configureEach {
            groupId = Project.GROUP
            version = Project.VERSION
        }

        register(Project.NAME, MavenPublication::class.java) {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/catgirlclient/DiscordInteraKTions/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}