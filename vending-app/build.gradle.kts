 plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

javafx {
    version = "18.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":licensing-lib"))
    implementation("org.jfxtras:jmetro:11.6.16")
    implementation("org.apache.commons:commons-csv:1.9.0")
}

application {
    mainClass.set("pt.ua.segurancainformatica.app.vending.Entrypoint")
}