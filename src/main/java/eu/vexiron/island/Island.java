package eu.vexiron.island;

import net.minestom.server.instance.InstanceContainer;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Island {

    private final UUID owner;
    private final IslandType type;
    private final long createdAt;
    private final Set<UUID> members = ConcurrentHashMap.newKeySet();

    private volatile InstanceContainer instance; // set when loaded, cleared when unloaded
    private volatile long lastVisited;

    public Island(UUID owner, IslandType type, long createdAt, long lastVisited) {
        this.owner = owner;
        this.type = type;
        this.createdAt = createdAt;
        this.lastVisited = lastVisited;
    }

    public UUID owner()          { return owner; }
    public IslandType type()     { return type; }
    public long createdAt()      { return createdAt; }
    public long lastVisited()    { return lastVisited; }
    public Set<UUID> members()   { return members; }
    public InstanceContainer instance() { return instance; }

    public void setInstance(InstanceContainer instance) { this.instance = instance; }
    public void touch() { this.lastVisited = System.currentTimeMillis(); }

    public boolean isLoaded() { return instance != null; }
}