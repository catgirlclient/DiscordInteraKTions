plugins {
    org.jetbrains.kotlin.jvm
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":platforms:gateway-kord"))
    implementation(project(":platforms:webserver-ktor-kord"))
    implementation(libs.kord.rest)
    implementation(libs.kord.gateway)
    implementation(libs.logback)
}

tasks.test {
    useJUnitPlatform()
}