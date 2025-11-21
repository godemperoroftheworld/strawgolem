plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.10.433" apply false
    id("dev.kikugie.j52j") version "1.0" apply false // Enables asset processing by writing json5 files
    id("me.modmuss50.mod-publish-plugin") version "0.5+" apply false // Publishes builds to hosting websites
    id("architectury-plugin") version "3.4-SNAPSHOT" apply false
    id("co.uzzu.dotenv.gradle") version "4.0.0"
}

stonecutter active "1.20.1-fabric" /* You may have to edit this. Make sure it matches one of the versions present in settings.gradle.kts */


