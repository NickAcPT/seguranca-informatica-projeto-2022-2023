plugins {
    java
    application
    id ("org.openjfx.javafxplugin") version "0.0.13"
}

javafx {
    version = "18.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.jfxtras:jmetro:11.6.16")
}

application {
    mainClass.set("pt.ua.segurancainformatica.app.vending.Entrypoint")
}