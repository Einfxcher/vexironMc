package eu.vexiron.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public enum Rank {
    PLAYER(0, null, null),
    VIP(10, "VIP", TextColor.color(0xF59E0B)),
    MVP(20, "MVP", TextColor.color(0x22D3EE)),
    LEGEND(30, "LEGEND", TextColor.color(0xE879F9)),
    MEDIA(45, "MEDIA", TextColor.color(0xFF4E45)),
    BUILDER(50, "BUILDER", TextColor.color(0x34D399)),
    DEVELOPER(55, "DEV", TextColor.color(0x818CF8)),
    HELPER(70, "HELPER", TextColor.color(0x4ADE80)),
    MODERATOR(80, "MOD", TextColor.color(0x60A5FA)),
    ADMIN(90, "ADMIN", TextColor.color(0xEF5350)),
    OWNER(100, "OWNER", TextColor.color(0x8B5CF6));

    private static final TextColor SEPARATOR_COLOR = TextColor.color(0x4A4A52);
    private static final TextColor NAME_COLOR = TextColor.color(0xEDEDED);

    private final int weight;
    private final String displayName;
    private final TextColor color;

    Rank(int weight, String displayName, TextColor color) {
        this.weight = weight;
        this.displayName = displayName;
        this.color = color;
    }

    public int weight() {
        return weight;
    }

    public String displayName() {
        return displayName == null ? "PLAYER" : displayName;
    }

    public TextColor color() {
        return color == null ? NAME_COLOR : color;
    }

    public Component prefix() {
        if (displayName == null) return Component.empty();
        return Component.textOfChildren(
                Component.text(displayName, color, TextDecoration.BOLD),
                Component.text(" · ", SEPARATOR_COLOR)
        );
    }

    public Component formatName(String username) {
        return Component.textOfChildren(prefix(), Component.text(username, NAME_COLOR));
    }

    public boolean isAtLeast(Rank other) {
        return this.weight >= other.weight;
    }

    public static Rank fromString(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PLAYER;
        }
    }
}