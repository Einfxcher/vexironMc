package eu.vexiron.rank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class JsonRankStorage implements RankStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonRankStorage.class);
    private static final Type FILE_TYPE = new TypeToken<Map<String, String>>() {}.getType();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path file;
    private final Map<UUID, Rank> cache = new ConcurrentHashMap<>();

    public JsonRankStorage(Path file) {
        this.file = file;
        ensureParent();
        cache.putAll(loadFromDisk());
    }

    @Override
    public Map<UUID, Rank> loadAll() {
        return new HashMap<>(cache);
    }

    @Override
    public synchronized void save(UUID uuid, Rank rank) {
        cache.put(uuid, rank);
        writeToDisk();
    }

    @Override
    public synchronized void delete(UUID uuid) {
        cache.remove(uuid);
        writeToDisk();
    }

    @Override
    public synchronized void saveAll(Map<UUID, Rank> ranks) {
        cache.clear();
        cache.putAll(ranks);
        writeToDisk();
    }

    private Map<UUID, Rank> loadFromDisk() {
        if (!Files.exists(file)) return new HashMap<>();

        try (Reader reader = Files.newBufferedReader(file)) {
            Map<String, String> raw = GSON.fromJson(reader, FILE_TYPE);
            if (raw == null) return new HashMap<>();

            Map<UUID, Rank> result = new HashMap<>();
            raw.forEach((key, value) -> {
                try {
                    result.put(UUID.fromString(key), Rank.fromString(value));
                } catch (IllegalArgumentException e) {
                    LOGGER.warn("Skipped invalid rank entry: {}", key);
                }
            });
            return result;
        } catch (IOException e) {
            LOGGER.error("Failed to load ranks from disk", e);
            return new HashMap<>();
        }
    }

    private void writeToDisk() {
        try {
            ensureParent();
            Map<String, String> raw = new HashMap<>();
            cache.forEach((uuid, rank) -> raw.put(uuid.toString(), rank.name()));

            Path temp = file.resolveSibling(file.getFileName() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(temp)) {
                GSON.toJson(raw, writer);
            }
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("Failed to save ranks to disk", e);
        }
    }

    private void ensureParent() {
        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to create parent directory for ranks file", e);
        }
    }
    @Override
    public synchronized void reload() {
        cache.clear();
        cache.putAll(loadFromDisk());
        LOGGER.info("Reloaded {} rank entries from disk", cache.size());
    }
}