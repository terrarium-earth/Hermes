import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.architectury.plugin.ArchitectPluginExtension
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

plugins {
    java
    id("maven-publish")
    id("dev.architectury.loom") version "1.6-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

architectury {
    val minecraftVersion: String by project
    minecraft = minecraftVersion
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")
    apply(plugin = "com.github.johnrengelman.shadow")

    val minecraftVersion: String by project
    val modLoader = project.name
    val modId = rootProject.name
    val isCommon = modLoader == rootProject.projects.common.name

    base {
        archivesName.set("$modId-$modLoader-$minecraftVersion")
    }

    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()
    }

    repositories {
        mavenCentral()
        maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
        maven(url = "https://maven.neoforged.net/releases/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://maven.dediamondpro.dev/releases")
        mavenLocal()
    }

    val shade: Configuration by configurations.creating {
        configurations.implementation.get().extendsFrom(this)
    }

    dependencies {
        val resourcefulLibVersion: String by project
        val mineMarkVersion: String by project
        val commonMarkVersion: String by project

        "minecraft"("::${minecraftVersion}")

        @Suppress("UnstableApiUsage")
        "mappings"(project.the<LoomGradleExtensionAPI>().layered {
            val parchmentVersion: String by project

            officialMojangMappings()

            parchment(create(group = "org.parchmentmc.data", name = "parchment-$minecraftVersion", version = parchmentVersion))
        })

        "modApi"(group = "com.teamresourceful.resourcefullib", name = "resourcefullib-$modLoader-$minecraftVersion", version = resourcefulLibVersion)
        shade("dev.dediamondpro:minemark-core:$mineMarkVersion")
        implementation("org.commonmark:commonmark:$commonMarkVersion")
        shade("org.commonmark:commonmark-ext-gfm-strikethrough:$commonMarkVersion") { isTransitive = false }
        shade("org.commonmark:commonmark-ext-gfm-tables:$commonMarkVersion") { isTransitive = false }
    }

    java {
        withSourcesJar()
    }

    tasks.jar {
        archiveClassifier.set("dev")
    }

    tasks.named<RemapJarTask>("remapJar") {
        archiveClassifier.set(null as String?)
        injectAccessWidener.set(true)
    }

    tasks.named<ShadowJar>("shadowJar") {
        relocate("dev.dediamondpro.minemark", "earth.terrarium.hermes.libs.minemark")
        relocate("org.commonmark", "earth.terrarium.hermes.libs.commonmark")
        relocate("org.ccil.cowan.tagsoup", "earth.terrarium.hermes.libs.tagsoup")
    }

    tasks.processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        filesMatching(listOf("META-INF/neoforge.mods.toml", "fabric.mod.json")) {
            expand("version" to project.version)
        }
    }

    if (!isCommon) {
        configure<ArchitectPluginExtension> {
            platformSetupLoomIde()
        }

        val shadowCommon by configurations.creating {
            isCanBeConsumed = false
            isCanBeResolved = true
        }

        tasks {
            "shadowJar"(ShadowJar::class) {
                archiveClassifier.set("dev-shadow")
                configurations = listOf(shadowCommon)

                exclude("architectury.common.json")
            }

            "remapJar"(RemapJarTask::class) {
                dependsOn("shadowJar")
                inputFile.set(named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
            }
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "$modId-$modLoader-$minecraftVersion"
                from(components["java"])

                pom {
                    name.set("Hermes $modLoader")
                    url.set("https://github.com/terrarium-earth/Hermes")

                    scm {
                        connection.set("git:https://github.com/terrarium-earth/Hermes.git")
                        developerConnection.set("git:https://github.com/terrarium-earth/Hermes.git")
                        url.set("https://github.com/terrarium-earth/Hermes")
                    }

                    licenses {
                        license {
                            name.set("MIT")
                        }
                    }
                }
            }
        }
        repositories {
            maven {
                setUrl("https://maven.teamresourceful.com/repository/terrarium/")
                credentials {
                    username = System.getenv("MAVEN_USER")
                    password = System.getenv("MAVEN_PASS")
                }
            }
        }
    }
}