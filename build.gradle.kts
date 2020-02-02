buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    checkstyle
}

subprojects {
    group = "com.openosrs.externals"

    project.extra["PluginProvider"] = "Ganom"
    project.extra["ProjectUrl"] = "https://discordapp.com/invite/hVPfVAR"
    project.extra["PluginLicense"] = "GNU General Public License v3.0"

    repositories {
        jcenter()
        mavenLocal()
        maven(url = "https://raw.githubusercontent.com/open-osrs/hosting/master/repo/stable/")
        maven(url = "https://raw.githubusercontent.com/open-osrs/hosting/master/")
        maven(url = "https://repo.runelite.net")
        maven(url = "https://jitpack.io")
    }

    apply<JavaPlugin>()
    apply(plugin = "checkstyle")

    checkstyle {
        maxWarnings = 0
        toolVersion = "8.25"
        isShowViolations = true
        isIgnoreFailures = false
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<Jar> {
            doLast {
                copy {
                    from("./build/libs/")
                    into("../release/")
                }
            }
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            dirMode = 493
            fileMode = 420
        }

        withType<Checkstyle> {
            group = "verification"
        }
    }
}