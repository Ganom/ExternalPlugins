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

tasks.named("bootstrapPlugins") {
    finalizedBy("copyBootstrap")
}

tasks.register<Copy>("copyBootstrap"){
    println("Copying bootstrap to main dir.")
    from("./build/bootstrap/")
    into("./")
    eachFile {
        if (this.relativePath.getFile(destinationDir).exists() && this.sourceName != "plugins.json") {
            this.exclude()
            println("Excluding " + this.sourceName + " as its the same version.")
        }
    }
}

subprojects {
    group = "com.openosrs.externals"

    project.extra["PluginProvider"] = "Ganom"
    project.extra["ProjectUrl"] = "https://ganomsoftware.com/"
    project.extra["PluginLicense"] = "GNU General Public License v3.0"

    repositories {
        maven(url = "https://repo.runelite.net")
        mavenLocal()
        mavenCentral()
        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://raw.githubusercontent.com/open-osrs/hosting/master")
                }
            }
            filter {
                includeModule("net.runelite", "fernflower")
                includeModule("com.openosrs.rxrelay3", "rxrelay")
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
            }
        }
    }

    apply<JavaPlugin>()
    apply<JavaLibraryPlugin>()
    apply(plugin = "checkstyle")

    val oprsVersion = "latest.release"

    dependencies {
        annotationProcessor("org.pf4j:pf4j:3.6.0")
        annotationProcessor("org.projectlombok:lombok:1.18.24")

        compileOnly(group = "com.openosrs", name = "http-api", version = oprsVersion)
        compileOnly(group = "com.openosrs", name = "runelite-api", version = oprsVersion)
        compileOnly(group = "com.openosrs", name = "runelite-client", version = oprsVersion)
        compileOnly(group = "com.openosrs.rs", name = "runescape-client", version = oprsVersion)
        compileOnly(group = "com.openosrs.rs", name = "runescape-api", version = oprsVersion)

        compileOnly("ch.qos.logback:logback-classic:1.2.3")
        compileOnly("com.google.code.gson:gson:2.8.6")
        compileOnly("com.google.guava:guava:30.0-jre")
        compileOnly("com.google.inject:guice:4.2.3:no_aop")
        compileOnly("org.apache.commons:commons-text:1.9")
        compileOnly("org.pf4j:pf4j:3.6.0")
        compileOnly("org.projectlombok:lombok:1.18.24")
        compileOnly("org.pushing-pixels:radiance-substance:2.5.1")
    }

    checkstyle {
        maxWarnings = 0
        toolVersion = "8.25"
        isShowViolations = true
        isIgnoreFailures = false
    }

    tasks {
        java {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
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
