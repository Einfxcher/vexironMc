package eu.vexiron.server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.Status;

public final class Motd {

    private static final Component DESCRIPTION = Component.text()
            .append(Component.text("Vexiron", NamedTextColor.AQUA))
            .append(Component.newline())
            .append(Component.text("Now open", NamedTextColor.GRAY))
            .build();

    Motd() {
        MinecraftServer.getGlobalEventHandler().addListener(
                ServerListPingEvent.class,
                event -> event.setStatus(
                        Status.builder(event.getStatus())
                                .description(DESCRIPTION)
                                .build()
                )
        );
    }
}