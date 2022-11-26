import net.ltgt.gradle.errorprone.errorprone

plugins {
    id("net.ltgt.errorprone") version "3.0.1" apply false
}

val errorproneVersion: String by extra
val jbAnnotationsVersion: String by extra
val nullawayVersion: String by extra

allprojects {
    this.group = "pt.ua.segurancainformatica"
    this.version = "1.0-SNAPSHOT"

    if (this == rootProject) return@allprojects

    apply(plugin = "java")
    apply(plugin = "net.ltgt.errorprone")

    // Add our repositories
    this.repositories {
        mavenCentral()
        flatDir {
            dir(rootDir.resolve("libraries"))
        }
    }

    /*
    Add our dependencies for all projects:
    - ErrorProne - For static analysis of Java code.
    - NullAway - For static analysis to detect nullability issues in Java code.
    - JetBrains' annotations - For helping the IDE to analyze the code better,
    - and providing @Nullable and @NotNull annotations for documentation.
    */
    this.dependencies {
        "errorprone"("com.google.errorprone:error_prone_core:$errorproneVersion")
        "errorprone"("com.uber.nullaway:nullaway:$nullawayVersion")
        "compileOnly"("org.jetbrains:annotations:$jbAnnotationsVersion")
        "testCompileOnly"("org.jetbrains:annotations:$jbAnnotationsVersion")
    }

    // Configure ErrorProne, enable NullAway and set encoding to UTF-8
    this.tasks.withType<JavaCompile>().configureEach {
        options.errorprone {
            option("NullAway:AnnotatedPackages", "pt.ua")
            option("NullAway:ExcludedFieldAnnotations", "javafx.fxml.FXML")
        }

        options.errorprone.error("NullAway")

        options.encoding = "UTF-8"
    }

    // For reproducible builds (https://reproducible-builds.org/docs/jvm/)
    this.tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }


    // Configure the toolchain to use Java 18
    this.extensions.getByType<JavaPluginExtension>().apply {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(18))
        }
    }
}