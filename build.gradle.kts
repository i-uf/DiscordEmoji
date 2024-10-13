plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "7.1.0" // Shadow 플러그인 추가
}

group = "com.i_uf"
version = "1.0.0"

repositories {
    mavenCentral()
}
dependencies {
    implementation(kotlin("stdlib"))
}
tasks.shadowJar {
    manifest {
        attributes(
            "Main-Class" to "com.i_uf.AppKt"
        )
    }
}