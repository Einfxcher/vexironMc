package eu.vexiron.server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;

public final class TabList {

    private static final Component HEADER = Component.text()
            .append(Component.newline())
            .append(Component.text("VEXIRON", NamedTextColor.AQUA, TextDecoration.BOLD))
            .append(Component.newline())
            .append(Component.text("          play.vexiron.eu          ", NamedTextColor.GRAY))
            .append(Component.newline())
            .build();

    TabList() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, _ -> updateAll());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, _ -> MinecraftServer.getSchedulerManager().scheduleNextTick(this::updateAll));
    }

    private Component buildFooter() {
        int online = MinecraftServer.getConnectionManager().getOnlinePlayers().size();

        return Component.text()
                .append(Component.newline())
                .append(Component.text("Online: ", NamedTextColor.GRAY))
                .append(Component.text(online, NamedTextColor.AQUA))
                .append(Component.newline())
                .build();
    }

    private void updateAll() {
        Component footer = buildFooter();
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> player.sendPlayerListHeaderAndFooter(HEADER, footer));
    }
}