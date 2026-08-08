package eu.vexiron.command;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.sound.SoundEvent;

public final class Feedback {

    private static final Sound SUCCESS = Sound.sound(SoundEvent.ENTITY_VILLAGER_YES, Sound.Source.PLAYER, 1f, 1f);
    private static final Sound ERROR = Sound.sound(SoundEvent.ENTITY_VILLAGER_NO, Sound.Source.PLAYER, 1f, 1f);

    private Feedback() {}

    public static void success(CommandSender sender, String message) {
        send(sender, message, NamedTextColor.GREEN, SUCCESS);
    }

    public static void error(CommandSender sender, String message) {
        send(sender, message, NamedTextColor.RED, ERROR);
    }

    private static void send(CommandSender sender, String message, NamedTextColor color, Sound sound) {
        sender.sendActionBar(Component.text(message, color));
        sender.playSound(sound, Sound.Emitter.self());
    }
}