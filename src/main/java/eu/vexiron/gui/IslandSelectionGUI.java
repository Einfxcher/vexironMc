package eu.vexiron.gui;

import eu.vexiron.command.Feedback;
import eu.vexiron.command.Text;
import eu.vexiron.island.IslandManager;
import eu.vexiron.island.IslandProvider;
import eu.vexiron.island.IslandType;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class IslandSelectionGUI {

    private static final Component TITLE = Component.text("Choose your Island");
    private static final ItemStack FILLER = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
            .customName(Component.empty())
            .build();
    private static final int[] SLOTS = { 10, 12, 14, 16 };
    private static final IslandType[] TYPES = IslandType.values();

    private final IslandProvider islands;

    public IslandSelectionGUI(IslandProvider islands) {
        this.islands = islands;
        registerListener();
    }

    private void registerListener() {
        MinecraftServer.getGlobalEventHandler().addListener(InventoryPreClickEvent.class, event -> {
            event.setCancelled(true);

            IslandType type = typeFromSlot(event.getSlot());
            if (type == null) return;

            Player player = event.getPlayer();
            player.closeInventory();

            IslandManager manager = islands.manager();
            manager.createIsland(player.getUuid(), type)
                    .thenCompose(island -> manager.teleportTo(player, island))
                    .thenRun(() -> Feedback.success(player, type.getDisplayName() + " created!"));
        });
    }

    public void open(Player player) {
        Inventory inv = new Inventory(InventoryType.CHEST_3_ROW, TITLE);
        for (int i = 0; i < inv.getSize(); i++) inv.setItemStack(i, FILLER);
        for (int i = 0; i < TYPES.length; i++) inv.setItemStack(SLOTS[i], buildIcon(TYPES[i]));
        player.openInventory(inv);
    }

    private ItemStack buildIcon(IslandType type) {
        List<String> lines = new ArrayList<>();
        Collections.addAll(lines, type.getDescription());
        lines.add("§aClick to choose!");

        return ItemStack.builder(type.getIcon())
                .customName(Text.title(type.getDisplayName(), type.getColor()))
                .lore(Text.lore(lines.toArray(new String[0])))
                .build();
    }

    private IslandType typeFromSlot(int slot) {
        for (int i = 0; i < SLOTS.length; i++) {
            if (SLOTS[i] == slot) return TYPES[i];
        }
        return null;
    }
}