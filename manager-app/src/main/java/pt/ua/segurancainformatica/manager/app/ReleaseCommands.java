package pt.ua.segurancainformatica.manager.app;

import picocli.CommandLine;
import pt.ua.segurancainformatica.manager.lib.release.ApplicationRelease;
import pt.ua.segurancainformatica.manager.lib.release.ApplicationReleaseManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@CommandLine.Command(name = "release", description = "Tools for managing releases")

public class ReleaseCommands {
    private static void printRelease(int i, ApplicationRelease release) {
        System.out.printf("%d. %s %s (%s)%n", i + 1, release.name(), release.version(), release.hash());
    }

    public static String getFileNameWithoutExtension(Path path) {
        var name = path.getFileName().toString();
        var dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) {
            return name;
        }
        return name.substring(0, dotIndex);
    }

    @CommandLine.Command(name = "list", description = "List all application releases.")
    public Integer listReleases() {
        var releases = ApplicationReleaseManager.INSTANCE.getValues();

        System.out.println("Application releases:");
        if (releases.isEmpty()) {
            System.out.println("No application releases found");
            return 1;
        } else {
            for (int i = 0; i < releases.size(); i++) {
                ApplicationRelease release = releases.get(i);
                printRelease(i, release);
            }
        }
        return 0;
    }

    @CommandLine.Command(name = "add", description = "Add a new application release.")
    public Integer addRelease(
            @CommandLine.Parameters(index = "0", description = "The jar that was released") Path releasedJar
    ) {
        try (var fs = FileSystems.newFileSystem(releasedJar)) {
            var buildInfo = fs.getPath("/build-information.properties");
            if (!Files.exists(buildInfo)) {
                System.out.println("The jar does not contain a build-information.properties file");
                return 1;
            }
            var file = Files.readString(buildInfo);
            var properties = new Properties();
            properties.load(new BufferedReader(new StringReader(file)));

            var name = properties.getProperty("name");
            var version = properties.getProperty("version");

            var hashFile = releasedJar.resolveSibling(getFileNameWithoutExtension(releasedJar) + ".sha256");

            if (!Files.exists(hashFile)) {
                System.out.println("The jar does not contain a hash file");
                return 1;
            }

            var hash = Files.readString(hashFile);

            var release = new ApplicationRelease(name, version, hash);
            ApplicationReleaseManager.INSTANCE.addValue(release);

            System.out.println("Application release added:" + release);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    @CommandLine.Command(name = "remove", description = "Remove an application release.")
    public Integer removeRelease(
            @CommandLine.Parameters(index = "0", description = "The index of the release to remove") int index
    ) {
        var releases = ApplicationReleaseManager.INSTANCE.getValues();
        var toRemove = releases.get(index - 1);

        ApplicationReleaseManager.INSTANCE.removeValue(toRemove);
        System.out.println("Application release removed:" + toRemove);
        return 0;
    }
}
