package eu.vexiron.island;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public final class IslandListener {

    IslandListener(IslandManager manager) {
        // Save any island a player was on when they disconnect
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
            // Find islands where this player is inside, save them
            // (auto-unload handles the rest via scheduler)
        });
    }
}