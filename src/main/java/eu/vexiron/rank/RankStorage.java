package eu.vexiron.rank;

import java.util.Map;
import java.util.UUID;

public interface RankStorage {
    Map<UUID, Rank> loadAll();
    void save(UUID uuid, Rank rank);
    void delete(UUID uuid);
    void saveAll(Map<UUID, Rank> ranks);
    void reload();
}