package eu.vexiron.rank;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerSpawnEvent;

public final class RankListener {

    RankListener(RankManager ranks) {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            if (event.isFirstSpawn()) {
                ranks.onJoin(event.getPlayer());
            }
        });
    }
}