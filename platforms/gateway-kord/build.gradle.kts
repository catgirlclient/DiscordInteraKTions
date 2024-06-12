plugins {
    live.shuuyu.scripts.`interaktions-module`
    live.shuuyu.scripts.`interaktions-publishing`
}

dependencies {
    implementation(libs.stdlib)
    api(project(":common"))
    implementation(libs.kord.rest)
    implementation(libs.kord.gateway)
}

tasks.test {
    useJUnitPlatform()
}