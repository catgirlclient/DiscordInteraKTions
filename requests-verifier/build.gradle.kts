plugins {
    live.shuuyu.scripts.`interaktions-module`
    live.shuuyu.scripts.`interaktions-publishing`
}

dependencies {
    implementation(libs.stdlib)
}

tasks.test {
    useJUnitPlatform()
}