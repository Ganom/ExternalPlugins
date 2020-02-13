import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

open class VersionTask : DefaultTask() {

    @Suppress("UnstableApiUsage")
    @set:Option(option = "newFiles", description = "The new files added in a PR")
    @get:Input
    var newFiles: String = ""

    @Suppress("UnstableApiUsage")
    @set:Option(option = "changedFiles", description = "The changed files added in a PR")
    @get:Input
    var changedFiles: String = ""

    private fun splitPath(path: String): String {
        return path.split("/")[0]
    }

    private fun filterPath(path: String): Boolean {
        val p = splitPath(path)
        val count = path.filter { it == '/' }
                .groupingBy { it }
                .eachCount()
                .getOrElse('/') { 0 }

        if (count < 9 || p.startsWith(".") || p.startsWith("buildsrc")) {
            return false
        }
        return true
    }

    private fun readFile(fileName: Path): List<String> = fileName.toFile().useLines { it.toList() }

    private fun writeFile(fileName: Path, content: List<String>) = fileName.toFile().writeText(content.joinToString(separator = System.lineSeparator()))

    private fun bumpVersion(path: Path) {
        val content = mutableListOf<String>()

        readFile(path).forEach {
            if (it.startsWith("version = ")) {
                val version = SemVer.parse(it.replace("\"", "").replace("version = ", ""))
                version.patch += 1
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
        val files = HashSet<String>()

        newFiles.split(" ").forEach {
            if (filterPath(it)) {
                files.add(splitPath(it))
            }
        }

        changedFiles.split(" ").forEach {
            if (filterPath(it)) {
                files.add(splitPath(it))
            }
        }

        if (files.size <= 0) {
            return
        }

        files.forEach {
            val path = project.childProjects[it]?.projectDir?.absolutePath
            val build = "$it.gradle.kts"

            if (path != null && !newFiles.contains(build) && !changedFiles.contains(build)) {
                val buildPath = Paths.get(path, build)

                if (Files.exists(buildPath)) {
                    bumpVersion(buildPath)
                }
            }
        }
    }
}
