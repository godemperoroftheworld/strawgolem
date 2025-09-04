import org.gradle.api.Project
import java.util.*

// --- Boolean helpers ---

fun Project.bool(str: String): Boolean =
    str.lowercase().startsWith("t")

fun Project.boolProperty(key: String): Boolean =
    if (hasProperty(key)) bool(property(key).toString()) else false

// --- List helpers ---

fun Project.listProperty(key: String): ArrayList<String> {
    if (!hasProperty(key)) return arrayListOf()
    val str = property(key).toString()
    if (str == "UNSET") return arrayListOf()
    return ArrayList(str.split(" "))
}

// --- String helpers ---

fun Project.optionalStrProperty(key: String): Optional<String> {
    if (!hasProperty(key)) return Optional.empty()
    val str = property(key).toString()
    return if (str == "UNSET") Optional.empty() else Optional.of(str)
}

// --- Version helpers ---

class VersionRange(val min: String, val max: String) {
    fun asForgelike(): String =
        "${if (min.isEmpty()) "(" else "["}${min},${max}${if (max.isEmpty()) ")" else "]"}"

    fun asFabric(): String =
        buildString {
            if (min.isNotEmpty()) append(">=$min")
            if (max.isNotEmpty()) {
                if (isNotEmpty()) append(" ")
                append("<=$max")
            }
        }
}

fun Project.versionProperty(key: String): VersionRange {
    if (!hasProperty(key)) return VersionRange("", "")

    val list = listProperty(key)
    list.replaceAll { if (it == "UNSET") "" else it }

    return when (list.size) {
        0 -> VersionRange("", "")
        1 -> VersionRange(list[0], "")
        else -> VersionRange(list[0], list[1])
    }
}

fun Project.optionalVersionProperty(key: String): Optional<VersionRange> {
    if (!hasProperty(key)) return Optional.empty()
    val str = optionalStrProperty(key)
    return if (!str.isPresent) Optional.empty() else Optional.of(versionProperty(key))
}
