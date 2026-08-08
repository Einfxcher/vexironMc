package eu.vexiron.server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.Status;

public final class Motd {

    private static final int MAX_PLAYERS = 2026;

    public Motd() {
        MinecraftServer.getGlobalEventHandler().addListener(
                ServerListPingEvent.class,
                event -> {
                    int online = MinecraftServer.getConnectionManager().getOnlinePlayerCount();

                    Component description = Component.text()
                            .append(Component.text("VEXIRON", NamedTextColor.AQUA, TextDecoration.BOLD))
                            .append(Component.newline())
                            .append(Component.text("In Development", NamedTextColor.YELLOW))
                            .append(Component.text(" — ", NamedTextColor.DARK_GRAY))
                            .append(Component.text("vexiron.eu", NamedTextColor.GRAY))
                            .build();

                    event.setStatus(Status.builder(event.getStatus())
                            .description(description)
                            .playerInfo(Status.PlayerInfo.builder()
                                    .onlinePlayers(online)
                                    .maxPlayers(MAX_PLAYERS)
                                    .build())
                            .build());
                }
        );
    }
}