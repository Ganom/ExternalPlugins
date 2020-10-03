buildscript {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

plugins {
    java
    checkstyle
}

apply<BootstrapPlugin>()
apply<VersionPlugin>()

subprojects {
    group = "com.openosrs.externals"

    project.extra["PluginProvider"] = "Ganom"
    project.extra["ProjectUrl"] = "https://discordapp.com/invite/gsoft"
    project.extra["PluginLicense"] = "GNU General Public License v3.0"

    repositories {
        jcenter {
            content {
                excludeGroupByRegex("com\\.openosrs.*")
                excludeGroupByRegex("com\\.runelite.*")
            }
        }

        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://repo.runelite.net")
                }
            }
            filter {
                includeModule("net.runelite", "discord")
                includeModule("net.runelite.jogl", "jogl-all")
                includeModule("net.runelite.gluegen", "gluegen-rt")
            }
        }

        exclusiveContent {
            forRepository {
                mavenLocal()
            }
            filter {
                includeGroupByRegex("com\\.openosrs.*")
            }
        }
    }
    apply<JavaPlugin>()
    apply<JavaLibraryPlugin>()
    apply(plugin = "checkstyle")

    dependencies {
        annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.12")
        annotationProcessor(group = "org.pf4j", name = "pf4j", version = "3.2.0")
        implementation(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
        implementation(group = "com.google.code.gson", name = "gson", version = "2.8.6")
        implementation(group = "com.google.guava", name = "guava", version = "28.2-jre")
        implementation(group = "com.google.inject", name = "guice", version = "4.2.3", classifier = "no_aop")
        implementation(group = "com.openosrs", name = "http-api", version = "3.4.5")
        implementation(group = "com.openosrs", name = "injected-client", version = "3.4.5")
        implementation(group = "com.openosrs", name = "runelite-api", version = "3.4.5")
        implementation(group = "com.openosrs", name = "runelite-client", version = "3.4.5")
        implementation(group = "com.openosrs.rs", name = "runescape-api", version = "3.4.5")
        implementation(group = "com.openosrs.rs", name = "runescape-client", version = "3.4.5")
        implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.5.0")
        implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.5.0")
        implementation(group = "io.reactivex.rxjava3", name = "rxjava", version = "3.0.2")
        implementation(group = "net.sf.jopt-simple", name = "jopt-simple", version = "5.0.4")
        implementation(group = "org.apache.commons", name = "commons-text", version = "1.8")
        implementation(group = "org.pf4j", name = "pf4j", version = "3.2.0")
        implementation(group = "org.projectlombok", name = "lombok", version = "1.18.12")
        implementation(group = "org.pushing-pixels", name = "radiance-substance", version = "2.5.1")
    }

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
