rootProject.name = "DiscordInteraKTions"

// Remove :platforms:webserver-ktor-kord and :requests-verifier for now as they break with compilation.
include(
    ":sample",
    ":common",
    ":requests-verifier",
    ":platforms:gateway-kord",
    ":platforms:webserver-ktor-kord"
)