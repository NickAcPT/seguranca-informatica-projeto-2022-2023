plugins {
    application
}

dependencies {
    implementation(project(":manager-lib"))
    implementation("info.picocli:picocli:4.7.0")
}

tasks {
    application {
        mainClass.set("pt.ua.segurancainformatica.manager.app.ManagerCommand")
    }
}