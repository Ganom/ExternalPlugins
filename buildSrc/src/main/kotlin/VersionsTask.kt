import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

open class VersionsTask : DefaultTask() {

    @Suppress("UnstableApiUsage")
    @set:Option(option = "module", description = "The module that should be updated")
    @get:Input
    var module: String = "All"

    @Suppress("UnstableApiUsage")
    @set:Option(option = "major", description = "Bump major version")
    @get:Input
    var major: Boolean = false

    @Suppress("UnstableApiUsage")
    @set:Option(option = "minor", description = "Bump minor version")
    @get:Input
    var minor: Boolean = true

    @Suppress("UnstableApiUsage")
    @set:Option(option = "patch", description = "Bump patch version")
    @get:Input
    var patch: Boolean = false

    private fun readFile(fileName: Path): List<String> = fileName.toFile().useLines { it.toList() }

    private fun writeFile(fileName: Path, content: List<String>) = fileName.toFile().writeText(content.joinToString(separator = System.lineSeparator()))

    private fun bumpVersion(path: Path) {
        val content = mutableListOf<String>()

        readFile(path).forEach {
            if (it.startsWith("version = ")) {
                val version = SemVer.parse(it.replace("\"", "").replace("version = ", ""))

                if (major) {
                    version.major += 1
                    version.minor = 0
                    version.patch = 0
                } else if (minor) {
                    version.minor += 1
                    version.patch = 0
                } else if (patch) {
                    version.patch += 1
                }

                content.add("version = \"$version\"")
            } else {
                content.add(it)
            }
        }

        if (content.size > 0) {
            writeFile(path, content)
        }
    }

    @TaskAction
    fun bump() {
        if (!major && !minor && !patch) {
            return
        }

        project.childProjects.forEach {
            if (module == "All" || it.key.toLowerCase() == module.toLowerCase()) {
                val path = it.value.projectDir.absolutePath
                val build = Paths.get(path, "${it.key}.gradle.kts")

                if (Files.exists(build)) {
                    bumpVersion(build)
                }
            }
        }
    }
}
