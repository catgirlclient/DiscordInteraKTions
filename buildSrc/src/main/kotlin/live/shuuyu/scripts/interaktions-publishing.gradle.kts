package live.shuuyu.scripts

import live.shuuyu.scripts.utils.Project

plugins {
    `maven-publish`
    signing
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = Project.GROUP
            version = Project.VERSION
        }

        register(Project.NAME, MavenPublication::class.java) {
            from(components["java"])
        }
    }

    repositories {
        mavenCentral()
        maven("https://maven.pkg.github.com/catgirlclient/DiscordInteraKTions/") {
            name = "GitHubPackages"
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}