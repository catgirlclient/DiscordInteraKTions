plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    `maven-publish`
}

group = "net.perfectdreams.discordinteraktions"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    api(libs.kord.common)
    api(libs.kord.rest)
    api(libs.kord.core)

    implementation(libs.serialization.json)
    implementation(libs.coroutines.core)
    implementation(libs.kotlin.logging)
}

publishing {
    publications {
        register("PerfectDreams", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}