plugins {
    live.shuuyu.scripts.`interaktions-module`
    live.shuuyu.scripts.`interaktions-publishing`
}

dependencies {
    implementation(kotlin("reflect"))
    api(project(":requests-verifier"))
    api(project(":common"))
    implementation(libs.kord.rest)
    implementation(libs.ktor.server.netty)
}

tasks.test {
    useJUnitPlatform()
}