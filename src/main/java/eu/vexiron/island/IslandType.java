package eu.vexiron.island;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.Material;

public enum IslandType {
    SKY("Sky Island", Material.GRASS_BLOCK, NamedTextColor.GREEN,
            "A classic floating island",
            "in the sky with grass and dirt.",
            "",
            "§ePerfect for beginners!"),
    OCEAN("Ocean Island", Material.WATER_BUCKET, NamedTextColor.BLUE,
            "A small sand island",
            "surrounded by endless ocean.",
            "",
            "§eBuild your empire in the Ocean!"),
    NETHER("Nether Island", Material.NETHERRACK, NamedTextColor.RED,
            "A netherrack platform",
            "in a sea of lava.",
            "",
            "§eSurvive the heat!"),
    STONE("Stone Island", Material.STONE, NamedTextColor.GRAY,
            "An entire world made",
            "of solid stone.",
            "",
            "§eMine everything!");

    private final String displayName;
    private final Material icon;
    private final NamedTextColor color;
    private final String[] description;

    IslandType(String displayName, Material icon, NamedTextColor color, String... description) {
        this.displayName = displayName;
        this.icon = icon;
        this.color = color;
        this.description = description;
    }

    public String getDisplayName() { return displayName; }
    public Material getIcon() { return icon; }
    public NamedTextColor getColor() { return color; }
    public String[] getDescription() { return description; }
}