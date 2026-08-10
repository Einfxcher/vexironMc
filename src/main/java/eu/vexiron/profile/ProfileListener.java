package eu.vexiron.profile;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public final class ProfileListener {

    ProfileListener(ProfileManager manager) {
        MinecraftServer.getGlobalEventHandler().addListener(
                AsyncPlayerConfigurationEvent.class,
                event -> manager.loadOrCreate(event.getPlayer())
        );

        MinecraftServer.getGlobalEventHandler().addListener(
                PlayerDisconnectEvent.class,
                event -> manager.unload(event.getPlayer())
        );
    }
}