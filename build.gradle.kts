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

    this.repositories {
        mavenCentral()
        flatDir {
            dir(rootDir.resolve("libraries"))
        }
    }

    this.dependencies {
        "errorprone"("com.google.errorprone:error_prone_core:$errorproneVersion")
        "compileOnly"("org.jetbrains:annotations:$jbAnnotationsVersion")
        "errorprone"("com.uber.nullaway:nullaway:$nullawayVersion")
    }

    tasks.withType<JavaCompile>().configureEach {
        options.errorprone {
            option("NullAway:AnnotatedPackages", "pt.ua")
        }

        options.errorprone.error("NullAway")

        options.encoding = "UTF-8"
    }


    extensions.getByType<JavaPluginExtension>().apply {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(18))
        }
    }
}