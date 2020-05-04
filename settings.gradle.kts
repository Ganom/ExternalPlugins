rootProject.name = "public"

include(":Anonymizer")
include(":AutoClicker")
include(":AutoPrayFlick")
include(":BossSwapper")
include(":CustomSwapper")
include(":ExtUtils")
include(":ItemDropper")
include(":LeftClickCast")
include(":NeverLog")
include(":OneClick")

for (project in rootProject.children) {
    project.apply {
        projectDir = file(name)
        buildFileName = "${name.toLowerCase()}.gradle.kts"

        require(projectDir.isDirectory) { "Project '${project.path} must have a $projectDir directory" }
        require(buildFile.isFile) { "Project '${project.path} must have a $buildFile build script" }
    }
}
