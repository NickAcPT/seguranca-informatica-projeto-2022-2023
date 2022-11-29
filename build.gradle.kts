import net.ltgt.gradle.errorprone.errorprone
import java.nio.file.Files
import java.security.MessageDigest
import java.util.*

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
        options.errorprone.disable("ObjectToString")

        options.encoding = "UTF-8"
    }

    // For reproducible builds (https://reproducible-builds.org/docs/jvm/)
    this.tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true

        this.createBuildInformationFile(this@allprojects)
    }

    // Configure the toolchain to use Java 18
    this.extensions.getByType<JavaPluginExtension>().apply {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(18))
        }
    }

    this.tasks.withType<AbstractArchiveTask>().forEach {
        createHashForArchiveTask(this@allprojects, it)
    }
}

fun createHashForArchiveTask(project: Project, task: AbstractArchiveTask) {
    project.tasks.create("createHashFor${task.name.capitalize()}") {
        group = "segurancainformatica"
        description = "Hashes the output of the ${task.name} task."
        dependsOn(task)

        doLast {
            val zipFile = task.outputs.files.singleFile

            val hash = MessageDigest.getInstance("SHA-256")
                    .digest(zipFile.readBytes())
                    .joinToString("") { "%02x".format(it) }

            val hashFile = zipFile.resolveSibling(zipFile.nameWithoutExtension + ".sha256")
            hashFile.writeText(hash)
        }

        task.finalizedBy(this)
    }
}

fun AbstractArchiveTask.createBuildInformationFile(project: Project) {
    val props = Properties()
    props["name"] = "fancyName".takeIf { project.extra.has(it) }?.let { project.extra[it] } as? String ?: project.name
    props["version"] = project.version

    Files.createDirectories(project.buildDir.toPath())
    val buildInformationFile = project.buildDir.resolve("${project.name.removePrefix("vending-app").takeIf { it.isNotEmpty() }?.let { "$it-" } ?: ""}build-information.properties")

    props.store(buildInformationFile.writer(), "Build information")
    from(buildInformationFile)
}
