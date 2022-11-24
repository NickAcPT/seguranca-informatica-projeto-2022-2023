dependencies {
    implementation(project(":citizen-card-lib"))
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-smile:2.14.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }

       // systemProperty("java.security.debug", "access,failure,properties,provider,sunpkcs11")
    }
}