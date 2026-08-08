package eu.vexiron.listener;

import eu.vexiron.world.InstanceProvider;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;

public class PlayerListener {

    private static final GameMode DEFAULT_GAMEMODE = GameMode.ADVENTURE;

    PlayerListener(InstanceProvider instances) {
        MinecraftServer.getGlobalEventHandler().addListener(
                AsyncPlayerConfigurationEvent.class,
                event -> {
                    var player = event.getPlayer();
                    event.setSpawningInstance(instances.hub());
                    player.setRespawnPoint(InstanceProvider.HUB_SPAWN);
                    player.setGameMode(DEFAULT_GAMEMODE);
                }
        );
    }
}