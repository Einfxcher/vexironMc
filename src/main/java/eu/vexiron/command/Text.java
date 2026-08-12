package eu.vexiron.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public final class Text {

    private Text() {}

    public static Component title(String text, NamedTextColor color) {
        return Component.text(text, color)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true);
    }

    public static Component line(String text) {
        return Component.text(text, NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> lore(String... lines) {
        List<Component> result = new ArrayList<>(lines.length);
        for (String line : lines) result.add(line(line));
        return result;
    }
}