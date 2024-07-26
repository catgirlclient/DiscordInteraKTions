package live.shuuyu.discordinteraktions.common.annotations

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.*

@DslMarker
@Retention(BINARY)
@Target(CLASS)
public annotation class InteraKTionsDsl

/**
 * Marks the given component as experimental, requiring opt-in for usage.
 */
@MustBeDocumented
@RequiresOptIn(
    "This is an experimental feature. You may experience issues in regards to the API changing or other " +
            "unforeseen issues."
)
@Retention(BINARY)
@Target(CLASS, PROPERTY, FUNCTION, TYPEALIAS, CONSTRUCTOR)
public annotation class InteraKTionsExperimental