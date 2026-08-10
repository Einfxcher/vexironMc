package eu.vexiron.profile;

import net.minestom.server.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ProfileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileManager.class);

    private final Map<UUID, PlayerProfile> cache = new ConcurrentHashMap<>();
    private final ProfileRepository repository;

    public ProfileManager(ProfileRepository repository) {
        this.repository = repository;
    }

    public PlayerProfile get(Player player) {
        return cache.get(player.getUuid());
    }

    public PlayerProfile get(UUID uuid) {
        return cache.get(uuid);
    }

    public PlayerProfile loadOrCreate(Player player) {
        UUID uuid = player.getUuid();
        String username = player.getUsername();

        PlayerProfile profile = repository.load(uuid).orElseGet(() -> {
            LOGGER.info("Creating new profile for {}", username);
            PlayerProfile fresh = PlayerProfile.create(uuid, username);
            repository.save(fresh);
            return fresh;
        });

        // Update username if changed
        if (!profile.username().equals(username)) {
            profile.setUsername(username);
        }

        profile.setLastJoin(System.currentTimeMillis());
        profile.startSession();
        cache.put(uuid, profile);
        return profile;
    }

    public void save(Player player) {
        PlayerProfile profile = cache.get(player.getUuid());
        if (profile == null) return;

        profile.endSession();
        repository.save(profile);
    }

    public void unload(Player player) {
        save(player);
        cache.remove(player.getUuid());
    }

    public int cachedCount() {
        return cache.size();
    }
}