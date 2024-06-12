plugins {
    live.shuuyu.scripts.`interaktions-module`
    live.shuuyu.scripts.`interaktions-publishing`
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.test {
    useJUnitPlatform()
}