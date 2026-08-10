package eu.vexiron.profile;

import java.util.UUID;

public final class PlayerProfile {

    private final UUID uuid;
    private String username;
    private final long firstJoin;
    private long lastJoin;
    private long playtime; // in seconds

    private transient long sessionStart; // not persisted

    public PlayerProfile(UUID uuid, String username, long firstJoin, long lastJoin, long playtime) {
        this.uuid = uuid;
        this.username = username;
        this.firstJoin = firstJoin;
        this.lastJoin = lastJoin;
        this.playtime = playtime;
    }

    public static PlayerProfile create(UUID uuid, String username) {
        long now = System.currentTimeMillis();
        return new PlayerProfile(uuid, username, now, now, 0);
    }

    public UUID uuid() { return uuid; }
    public String username() { return username; }
    public long firstJoin() { return firstJoin; }
    public long lastJoin() { return lastJoin; }
    public long playtime() { return playtime; }

    public void setUsername(String username) { this.username = username; }
    public void setLastJoin(long lastJoin) { this.lastJoin = lastJoin; }

    /**
     * Called when the player joins. Starts the session timer.
     */
    public void startSession() {
        this.sessionStart = System.currentTimeMillis();
    }

    /**
     * Called when the player leaves. Adds session time to total playtime.
     */
    public void endSession() {
        if (sessionStart > 0) {
            playtime += (System.currentTimeMillis() - sessionStart) / 1000;
            sessionStart = 0;
        }
    }
}