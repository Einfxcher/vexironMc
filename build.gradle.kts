plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.8"
}

group = "eu.vexiron"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.07.22-26.2")
    implementation("ch.qos.logback:logback-classic:1.6.1")
    implementation("com.google.code.gson:gson:2.11.0")
}

tasks {
    shadowJar {
        manifest {
            attributes["Main-Class"] = "eu.vexiron.Main"
        }
        archiveFileName.set("vexiron.jar")
        mergeServiceFiles()
    }

    test {
        useJUnitPlatform()
    }
}