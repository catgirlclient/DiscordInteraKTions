plugins {
    live.shuuyu.scripts.`interaktions-module`
    live.shuuyu.scripts.`interaktions-publishing`
}

dependencies {
    implementation(libs.stdlib)
    implementation(libs.reflect)

    api(libs.kord.common)
    api(libs.kord.rest)
    api(libs.kord.core)

    implementation(libs.serialization.json)
    implementation(libs.coroutines.core)
    implementation(libs.kotlin.logging)
}