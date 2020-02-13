data class SemVer(
        var major: Int = 0,
        var minor: Int = 0,
        var patch: Int = 0
) : Comparable<SemVer> {
    companion object {
        @JvmStatic
        fun parse(version: String): SemVer {
            val pattern = Regex("""(0|[1-9]\d*)?(?:\.)?(0|[1-9]\d*)?(?:\.)?(0|[1-9]\d*)?(?:-([\dA-z\-]+(?:\.[\dA-z\-]+)*))?(?:\+([\dA-z\-]+(?:\.[\dA-z\-]+)*))?""")
            val result = pattern.matchEntire(version)
                    ?: throw IllegalArgumentException("Invalid version string [$version]")
            return SemVer(
                    major = if (result.groupValues[1].isEmpty()) 0 else result.groupValues[1].toInt(),
                    minor = if (result.groupValues[2].isEmpty()) 0 else result.groupValues[2].toInt(),
                    patch = if (result.groupValues[3].isEmpty()) 0 else result.groupValues[3].toInt()
            )
        }
    }

    init {
        require(major >= 0) { "Major version must be a positive number" }
        require(minor >= 0) { "Minor version must be a positive number" }
        require(patch >= 0) { "Patch version must be a positive number" }
    }

    override fun toString(): String = buildString {
        append("$major.$minor.$patch")
    }

    override fun compareTo(other: SemVer): Int {
        if (major > other.major) return 1
        if (major < other.major) return -1
        if (minor > other.minor) return 1
        if (minor < other.minor) return -1
        if (patch > other.patch) return 1
        if (patch < other.patch) return -1

        return -1
    }
}