package eu.vexiron.listener;

import eu.vexiron.rank.Rank;
import eu.vexiron.rank.RankManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;

public final class ChatListener {

    private static final TextColor SEPARATOR = TextColor.color(0x4A4A52);
    private static final TextColor MESSAGE = TextColor.color(0xBFBFBF);

    ChatListener(RankManager ranks) {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent.class, event -> {
            Player player = event.getPlayer();
            Rank rank = ranks.getRank(player);

            event.setFormattedMessage(Component.textOfChildren(
                    rank.formatName(player.getUsername()),
                    Component.text(" » ", SEPARATOR),
                    Component.text(event.getRawMessage(), MESSAGE)
            ));
        });
    }
}