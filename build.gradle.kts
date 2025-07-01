plugins {
    id("org.jetbrains.intellij") version "1.13.3"
    kotlin("jvm") version "1.8.20"
}

group = "com.yourorg"
version = "1.0.0"

repositories {
    mavenCentral()
}

intellij {
    version.set("2023.2")
    plugins.set(listOf("java"))
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
