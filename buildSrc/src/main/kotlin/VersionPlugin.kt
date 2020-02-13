import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class VersionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register<VersionTask>("bumpVersion") {}
        project.tasks.register<VersionsTask>("bumpVersions") {}
    }
}