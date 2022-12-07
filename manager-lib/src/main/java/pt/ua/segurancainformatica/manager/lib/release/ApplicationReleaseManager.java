package pt.ua.segurancainformatica.manager.lib.release;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;
import pt.ua.segurancainformatica.manager.lib.ManagerLicensingConstants;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ApplicationReleaseManager {

    private static final SmileMapper mapper = new SmileMapper();
    private static final CollectionType releasesListType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, ApplicationRelease.class);
    private ApplicationReleaseManager() {
        throw new IllegalStateException("Utility class");
    }

    public static List<ApplicationRelease> getApplicationReleases() {
        return List.copyOf(getReleases());
    }

    public static void addRelease(ApplicationRelease release) {
        var releases = getReleases();
        releases.add(release);
        saveReleases(releases);
    }

    private static void saveReleases(ArrayList<ApplicationRelease> releases) {
        try {
            mapper.writeValue(ManagerLicensingConstants.RELEASES_DATABASE_PATH.toFile(), releases);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save releases database", e);
        }
    }

    private static ArrayList<ApplicationRelease> getReleases() {
        if (Files.exists(ManagerLicensingConstants.RELEASES_DATABASE_PATH)) {
            try {
                return mapper.readValue(ManagerLicensingConstants.RELEASES_DATABASE_PATH.toFile(), releasesListType);
            } catch (Exception e) {
                throw new RuntimeException("Unable to read releases database", e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    public static void removeRelease(ApplicationRelease toRemove) {
        var releases = getReleases();
        releases.remove(toRemove);
        saveReleases(releases);
    }
}
