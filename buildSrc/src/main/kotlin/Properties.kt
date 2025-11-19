import org.gradle.api.Project

/**
 * Stores core dependency and environment information.
 */
class Env(private val project: Project) {

    val archivesBaseName = project.property("archives_base_name").toString()

    val mcVersion = project.versionProperty("deps.core.mc.version_range")
    val baseVersion = project.property("deps.core.mc.base_version").toString()

    val loader = project.property("loom.platform").toString()
    val isFabric = loader == "fabric"
    val isForge = loader == "forge"
    val isNeo = loader == "neoforge"

    val javaVer = if(atMost("1.16.5")) 8 else if(atMost("1.20.4")) 17 else 21

    fun atLeast(version: String) = project.stonecutterBuild.compare(mcVersion.min, version) >= 0
    fun atMost(version: String) = project.stonecutterBuild.compare(mcVersion.min, version) <= 0
    fun isNot(version: String) = project.stonecutterBuild.compare(mcVersion.min, version) != 0
    fun isExact(version: String) = project.stonecutterBuild.compare(mcVersion.min, version) == 0
}
// Stores information about the mod itself.
class ModProperties(project: Project) {
    val id = project.property("mod.id").toString()
    val displayName = project.property("mod.display_name").toString()
    val version = project.property("version").toString()
    val description = project.property("mod.description").toString()
    val authors = project.property("mod.authors").toString()
    val icon = project.property("mod.icon").toString()
    val issueTracker = project.property("mod.issue_tracker").toString()
    val license = project.property("mod.license").toString()
    val sourceUrl = project.property("mod.source_url").toString()
}
// Stores fabric-specific information
class ModFabric(project: Project) {
    val loaderVersion = project.versionProperty("deps.core.fabric.loader.version_range").min;
    val version = project.versionProperty("deps.core.fabric.version_range").min;
}
// Stores forge specific information
class ModForge(project: Project) {
    val loaderVersion = project.versionProperty("deps.core.forge.loader.version_range").min;
    val version = project.versionProperty("deps.core.forge.version_range").min;
}