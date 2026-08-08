package eu.vexiron.world;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;

public final class InstanceProvider {

    public static final Pos HUB_SPAWN = new Pos(0.5, 64, 0.5, 0, 0);
    private final HubInstance hub;

    public InstanceProvider() {
        this.hub = new HubInstance();
    }

    public InstanceContainer hub() {
        return hub.get();
    }
}