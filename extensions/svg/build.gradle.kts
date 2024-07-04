import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

val common: Configuration by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
}

val extShade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    common(project(":common", configuration = "namedElements"))

    extShade("org.apache.xmlgraphics:batik-transcoder:1.17") {
        exclude(group = "xml-apis", module = "xml-apis")
        exclude(group = "commons-io")
        exclude(group = "commons-logging")
    }
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.apache.batik", "earth.terrarium.hermessvg.libs.batik")
    relocate("org.apache.xmlgraphics", "earth.terrarium.hermessvg.libs.xmlgraphics")
    relocate("org.w3c.dom.svg", "earth.terrarium.hermessvg.libs.w3c.dom.svg")
    relocate("org.w3c.dom.smil", "earth.terrarium.hermessvg.libs.w3c.dom.smil")
    relocate("org.w3c.css", "earth.terrarium.hermessvg.libs.w3c.css")

    archiveClassifier.set("dev-shadow")
    configurations = listOf(extShade)
}

tasks.named<RemapJarTask>("remapJar") {
    dependsOn("shadowJar")
    inputFile.set(tasks.named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
}