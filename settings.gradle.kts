rootProject.name = "DiscordInteraKTions"

// Remove :platforms:webserver-ktor-kord and :requests-verifier for now as they break with compilation.
include(
    ":sample",
    ":common",
    ":platforms:gateway-kord"
)