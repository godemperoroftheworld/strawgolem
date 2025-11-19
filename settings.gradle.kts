
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://repo.spongepowered.org/maven")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.kikugie.dev/releases")
    }
    plugins {
        kotlin("jvm") version "1.9.24"
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.7"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    shared {
        version("1.19.2-fabric","1.19.2")
//        version("1.19.2-forge","1.19.2")
//        version("1.19.4-fabric","1.19.4")
//        version("1.19.4-forge","1.19.4")
//        version("1.20.1-fabric","1.20.1")
//        version("1.20.1-forge","1.20.1")
//        version("1.20.4-fabric","1.20.4")
//        version("1.20.4-neoforge","1.20.4")
//        version("1.20.6-fabric","1.20.6")
//        version("1.20.6-neoforge","1.20.6")
//        version("1.21.1-fabric","1.21.1")
//        version("1.21.1-neoforge","1.21.1")
//        version("1.21.2+3-fabric","1.21.2")
//        version("1.21.2+3-neoforge","1.21.2")
//        version("1.21.4-fabric", "1.21.4")
//        version("1.21.4-neoforge", "1.21.4")
//        version("1.21.5-fabric", "1.21.5")
//        version("1.21.5-neoforge", "1.21.5")
//        version("1.21.6+8-fabric", "1.21.6")
//        version("1.21.6+8-neoforge", "1.21.6")
        vcsVersion="1.19.2-fabric"
    }
    create(rootProject)
}

rootProject.name = "Straw Golem"




