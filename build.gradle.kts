// Plugins
plugins {
    kotlin("jvm") version "2.2.10"
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin")
}

// Repositories
repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.shedaniel.me/")
    maven( "https://maven.terraformersmc.com/releases/")
    maven("https://maven.nucleoid.xyz/") { name = "Nucleoid" }
    maven("https://api.modrinth.com/maven") { name = "Modrinth" }
    maven("https://cursemaven.com") { name = "CurseMaven" }
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") {
        name = "GeckoLib"
    }

}

val env = Env(project)
val mod = ModProperties(project)
val modFabric = ModFabric(project)
val modForge = ModForge(project)

version = "${mod.version}+${env.mcVersion.min}+${env.loader}"
group = property("group").toString()

stonecutter {
    constants {
        set("fabric", env.isFabric)
        set("forge", env.isForge)
        set("neoforge", env.isNeo)
    }

    replacements.string {
        direction = eval(current.version, ">=1.19.4")
        replace("AnimationBuilder", "RawAnimation")
    }
}

loom {
    silentMojangMappingsLicense()

    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    // Forge Mixin
    if (env.isForge) {
        forge {
            mixinConfigs("mixins.${mod.id}.json", "mixins.${mod.id}.forge.json")
        }
    }

    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
    }
}
base { archivesName.set(env.archivesBaseName) }

/** Dependencies **/
class Relation(
    val group: String,
    val module: String,
    val version: VersionRange,
    val friendlyVersion: VersionRange = version,
    val exclude: String = "",
    val loader: String = "",
    val modID: String = module,
    val optional: Boolean = false,
    val modrinth: String = modID,
    val curseforge: String = modID,
) {

    fun fabric(): String {
        return "\"${modID}\": \">=${friendlyVersion.min}\""
    }

    fun forge(): String {
        return "[[dependencies.${mod.id}]]\n" +
                "modId=\"${modID}\"\n" +
                "mandatory=${!optional}\n" +
                "versionRange=\"[${friendlyVersion.min},)\"\n" +
                "ordering=\"${if (optional) "NONE" else "AFTER"}\"\n" +
                "side=\"BOTH\"\n"
    }
}
val relations: Array<Relation> = arrayOf(
    Relation("com.terraformersmc",
        "modmenu",
        versionProperty("deps.api.mod_menu"),
        versionProperty("deps.api.mod_menu"),
        "",
        "fabric",
        modrinth = "mOgUt4GM",
        optional = true),
    Relation("me.shedaniel.cloth",
        "cloth-config-${env.loader}",
        versionProperty("deps.api.cloth_config"),
        versionProperty("deps.api.cloth_config"),
        optional = true,
        modID = if (env.isFabric) "cloth-config" else "cloth_config",
        modrinth = "9s6osm5g",
        curseforge = "cloth-config"),
    Relation("curse.maven",
        "jade-324717",
        versionProperty("deps.api.jade"),
        versionProperty("deps.api.friendly.jade"),
        optional = true,
        modrinth = "nvQzSEkH"),
    Relation("curse.maven",
        "animal-feeding-trough-445838",
        versionProperty("deps.api.animal_feeding_trough"),
        versionProperty("deps.api.friendly.animal_feeding_trough"),
        optional = true,
        loader = if (env.atLeast("1.20")) "" else "fabric",
        modrinth = "bRFWnJ87"),
    Relation("curse.maven",
        "architectury-api-419699",
        versionProperty("deps.api.architectury_api"),
        versionProperty("deps.api.friendly.architectury_api"),
        optional = true,
        modrinth = "lhGA9TYQ"),
    Relation("maven.modrinth",
        "haybale",
        versionProperty("deps.api.haybale"),
        versionProperty("deps.api.friendly.haybale"),
        modID = "haybale",
        optional = false,
        modrinth = "ZrllRmem"),
    Relation("software.bernie.geckolib",
        "geckolib-${env.loader}-${env.baseVersion}",
        versionProperty("deps.api.geckolib"),
        versionProperty("deps.api.geckolib"),
        optional = false,
        modID = if (env.atLeast("1.19.4")) "geckolib" else "geckolib3",
        modrinth = "8BmcQJ2H"),
)

dependencies {
    minecraft("com.mojang:minecraft:${env.mcVersion.min}")
    mappings(loom.officialMojangMappings())
    // Base Dependencies
    if(env.isFabric) {
        modImplementation("net.fabricmc:fabric-loader:${modFabric.loaderVersion}")
        modApi("net.fabricmc.fabric-api:fabric-api:${modFabric.version}")
    }
    if(env.isForge) {
        "forge"("net.minecraftforge:forge:${modForge.version}")
    }
    if(env.isNeo) {
        "neoForge"("net.neoforged:neoforge:${modForge.version}")
    }
    // Shadow ini4j
    implementation("org.ini4j:ini4j:0.5.4")
    include("org.ini4j:ini4j:0.5.4")
    if (env.isForge || env.isNeo) {
        "forgeRuntimeLibrary"("org.ini4j:ini4j:0.5.4")
    }
    if (env.isForge || env.isNeo) {
        modImplementation("com.eliotlash.mclib:mclib:20")
        "forgeRuntimeLibrary"("com.eliotlash.mclib:mclib:20")
    }
    // Decompiler
    vineflowerDecompilerClasspath("org.vineflower:vineflower:1.10.1")
    // APIs
    relations.forEach {
        if (it.loader.isBlank() || it.loader == env.loader) {
            val string = "${it.group}:${it.module}:${it.version.min}"
            if (it.optional) {
                modApi(string) {
                    exclude(group = "net.fabricmc")
                    if (it.exclude.isNotBlank()) {
                        exclude(group = it.exclude)
                    }
                }
            } else {
                modImplementation(string) {
                    exclude(group = "net.fabricmc")
                    if (it.exclude.isNotBlank()) {
                        exclude(group = it.exclude)
                    }
                }
            }
        }
    }
}

java {
    withSourcesJar()
    val java = if(env.javaVer == 8) JavaVersion.VERSION_1_8 else if(env.javaVer == 17) JavaVersion.VERSION_17 else JavaVersion.VERSION_21
    targetCompatibility = java
    sourceCompatibility = java
}

/**
 * Replaces the normal copy task and post-processes the files.
 * Effectively renames datapack directories due to depluralization past 1.20.4.
 */
abstract class ProcessResourcesExtension : ProcessResources() {
    @get:Input
    val autoPluralize = arrayListOf(
        "/data/minecraft/tags/block",
        "/data/minecraft/tags/item",
        "/data/strawgolem/loot_table",
        "/data/strawgolem/recipe",
        "/data/strawgolem/tags/block",
        "/data/strawgolem/tags/item",
    )
    override fun copy() {
        super.copy()
        val root = destinationDir.absolutePath
        autoPluralize.forEach { path ->
            val file = File(root.plus(path))
            if(file.exists()){
                file.copyRecursively(File(file.absolutePath.plus("s")),true)
                file.deleteRecursively()
            }
        }
    }
}

if(env.atMost("1.20.6")){
    tasks.replace("processResources",ProcessResourcesExtension::class)
}

tasks.processResources {
    dependsOn("stonecutterGenerate")

    val map = mapOf(
        "modid" to mod.id,
        "id" to mod.id,
        "name" to mod.displayName,
        "display_name" to mod.displayName,
        "version" to mod.version,
        "description" to mod.description,
        "authors" to mod.authors,
        "github_url" to mod.sourceUrl,
        "source_url" to mod.sourceUrl,
        "icon" to mod.icon,
        "mc_min" to env.mcVersion.min,
        "mc_max" to env.mcVersion.max,
        "issue_tracker" to mod.issueTracker,
        "java_ver" to env.javaVer.toString(),
        "loader_id" to env.loader,
        "license" to mod.license,
        "forgelike_loader_ver" to modForge.loaderVersion,
    )
    map.forEach{ (key, value) ->
        inputs.property(key,value)
    }

    // Dependencies
    val apisForLoader = relations.filter { it.loader.isBlank() || env.loader == it.loader }
    val required = apisForLoader.filter { !it.optional }
    val optional = apisForLoader.filter { it.optional }
    fun fabricDependencies(apis: List<Relation>): String {
        var result = apis.joinToString(separator = ",\n    ") { it.fabric() }
        return "{\n    $result\n  }"
    }
    fun forgeDependencies(apis: List<Relation>): String {
        return apis.joinToString(separator = "\n") { it.forge() }
    }

    filesMatching("pack.mcmeta") { expand(map) }
    filesMatching("fabric.mod.json") {
        expand(map + mapOf(
            "depends" to fabricDependencies(required),
            "recommends" to fabricDependencies(optional)
        ))
    }
    filesMatching("META-INF/mods.toml") { expand(map + mapOf(
        "depends" to forgeDependencies(apisForLoader)
    )) }
    filesMatching("META-INF/neoforge.mods.toml") { expand(map + mapOf(
        "depends" to forgeDependencies(apisForLoader)
    )) }
    filesMatching("META-INF/services/**") { expand(mapOf(
        "loader" to env.loader
    )) }
}

/**
 * Controls publishing. For publishing to work dryRunMode must be false.
 * Modrinth and Curseforge project tokens are publicly accessible, so it is safe to include them in files.
 * Do not include your API keys in your project!
 *
 * The Modrinth API token should be stored in the MODRINTH_TOKEN environment variable.
 * The curseforge API token should be stored in the CURSEFORGE_TOKEN environment variable.
 */
class ModPublish {
    val mcTargets = listProperty("publish.mc_targets")
    val modrinthProjectToken = property("publish.token.modrinth").toString()
    val curseforgeProjectToken = property("publish.token.curseforge").toString()
    val dryRunMode = boolProperty("publish.dry_run")
}
val modPublish = ModPublish()
publishMods {
    file = tasks.remapJar.get().archiveFile
    additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)
    displayName = "${mod.displayName} ${mod.version} for ${env.mcVersion.min}${if (env.mcVersion.max.isBlank()) "" else "-${env.mcVersion.max}" }"
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = if (mod.version.contains("alpha")) ALPHA else if (mod.version.contains("beta")) BETA else STABLE
    modLoaders.add(env.loader)
    dryRun = modPublish.dryRunMode
    version = mod.version

    modrinth {
        projectId = modPublish.modrinthProjectToken
        // Get one here: https://modrinth.com/settings/pats, enable read, write, and create Versions ONLY!
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.addAll(modPublish.mcTargets)
        relations.forEach{ src ->
            if (src.optional) {
                optional(src.modrinth)
            } else {
                requires(src.modrinth)
            }
        }
    }

    curseforge {
        projectId = modPublish.curseforgeProjectToken
        // Get one here: https://legacy.curseforge.com/account/api-tokens
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.addAll(modPublish.mcTargets)

        relations.forEach { src ->
            if (src.optional) {
                optional(src.curseforge)
            } else {
                requires(src.curseforge)
            }
        }

    }
}