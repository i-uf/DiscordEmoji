plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "DiscordEmoji"
include("src:main:java.module_info")
findProject(":src:main:java.module_info")?.name = "java.module_info"
include("src:main:java")
findProject(":src:main:java")?.name = "java"
include("src:main:java:module")
findProject(":src:main:java:module")?.name = "module"
