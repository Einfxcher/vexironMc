package eu.vexiron.command.commands;

import eu.vexiron.command.Feedback;
import eu.vexiron.profile.PlayerProfile;
import eu.vexiron.profile.ProfileManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ProfileCommand extends Command {

    private static final DateTimeFormatter DATE = DateTimeFormatter
            .ofPattern("MMM d, yyyy")
            .withZone(ZoneId.systemDefault());

    public ProfileCommand(ProfileManager profiles) {
        super("profile", "stats", "me");

        setDefaultExecutor((sender, ctx) -> {
            if (!(sender instanceof Player player)) {
                Feedback.error(sender, "Only players can use this command.");
                return;
            }

            PlayerProfile profile = profiles.get(player);
            if (profile == null) {
                Feedback.error(player, "Profile not loaded.");
                return;
            }

            player.sendMessage(Component.text()
                    .append(Component.text("─── ", NamedTextColor.DARK_GRAY))
                    .append(Component.text("Profile", NamedTextColor.AQUA))
                    .append(Component.text(" ───", NamedTextColor.DARK_GRAY))
                    .append(Component.newline())
                    .append(line("Name", profile.username()))
                    .append(line("First join", DATE.format(Instant.ofEpochMilli(profile.firstJoin()))))
                    .append(line("Playtime", formatPlaytime(profile.playtime())))
                    .build());
        });
    }

    private Component line(String label, String value) {
        return Component.text()
                .append(Component.text(label + ": ", NamedTextColor.GRAY))
                .append(Component.text(value, NamedTextColor.WHITE))
                .append(Component.newline())
                .build();
    }

    private String formatPlaytime(long seconds) {
        Duration d = Duration.ofSeconds(seconds);
        long hours = d.toHours();
        long minutes = d.toMinutesPart();

        if (hours > 0) return hours + "h " + minutes + "m";
        return minutes + "m";
    }
}