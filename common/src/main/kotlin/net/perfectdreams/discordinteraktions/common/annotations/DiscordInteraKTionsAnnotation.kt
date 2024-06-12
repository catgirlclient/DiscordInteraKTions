package net.perfectdreams.discordinteraktions.common.annotations

import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

@DslMarker
@Retention(BINARY)
@Target(CLASS)
public annotation class InteraKTionsDsl

@MustBeDocumented
@RequiresOptIn(
    "This is an experimental feature. You may experience issues in regards to the API changing or other " +
            "unforeseen issues."
)
@Retention(BINARY)
@Target(CLASS, PROPERTY, FUNCTION, TYPEALIAS, CONSTRUCTOR)
public annotation class InteraKTionsExperimental