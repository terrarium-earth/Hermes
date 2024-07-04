plugins {
    java
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

repositories {
    mavenCentral()
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    // TODO depend on common

    shade("org.apache.xmlgraphics:batik-transcoder:1.17") {
        exclude(group = "xml-apis", module = "xml-apis")
        exclude(group = "commons-io")
        exclude(group = "commons-logging")
    }
}

java {
    withSourcesJar()
}

tasks.jar {
    archiveClassifier.set("dev")
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.apache.batik", "earth.terrarium.hermes.libs.batik")
    relocate("org.apache.xmlgraphics", "earth.terrarium.hermes.libs.xmlgraphics")
    relocate("org.w3c.dom.svg", "earth.terrarium.hermes.libs.w3c.dom.svg")
    relocate("org.w3c.dom.smil", "earth.terrarium.hermes.libs.w3c.dom.smil")
    relocate("org.w3c.css", "earth.terrarium.hermes.libs.w3c.css")

    configurations = listOf(shade)
}