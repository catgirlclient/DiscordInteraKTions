plugins {
    live.shuuyu.scripts.`interaktions-module`
    live.shuuyu.scripts.`interaktions-publishing`
}

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":common"))
    implementation(libs.kord.rest)
    implementation(libs.kord.gateway)
}

tasks.test {
    useJUnitPlatform()
}