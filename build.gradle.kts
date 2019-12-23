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

    repositories {
        maven {
            url = uri("https://dl.bintray.com/oprs/")
        }
        jcenter()
        mavenLocal()
        mavenCentral()
    }

    apply<JavaLibraryPlugin>()
    apply(plugin = "checkstyle")

    checkstyle {
        maxWarnings = 0
        toolVersion = "8.25"
        isShowViolations = true
        isIgnoreFailures = false
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
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