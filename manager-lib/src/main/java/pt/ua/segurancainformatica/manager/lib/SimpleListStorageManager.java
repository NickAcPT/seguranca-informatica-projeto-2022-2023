package pt.ua.segurancainformatica.manager.lib;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SimpleListStorageManager<T> {
    protected static final SmileMapper mapper = new SmileMapper();
    private final Path dataPath;
    protected final CollectionType valueListType;

    public SimpleListStorageManager(Path dataPath, Class<T> elementClass) {
        this.dataPath = dataPath;
        valueListType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, elementClass);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        mapper.registerModule(javaTimeModule);
    }

    public void addValue(T value) {
        var values = getValues();
        values.add(value);
        saveValues(values);
    }

    private void saveValues(ArrayList<T> values) {
        try {
            mapper.writeValue(dataPath.toFile(), values);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save values database", e);
        }
    }

    public void removeValue(T toRemove) {
        var releases = getValues();
        releases.remove(toRemove);
        saveValues(releases);
    }

    public ArrayList<T> getValues() {
        if (Files.exists(dataPath)) {
            try {
                return mapper.readValue(dataPath.toFile(), valueListType);
            } catch (Exception e) {
                throw new RuntimeException("Unable to read values database", e);
            }
        } else {
            return new ArrayList<>();
        }
    }
}
