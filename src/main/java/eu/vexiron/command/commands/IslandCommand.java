package eu.vexiron.command.commands;

import eu.vexiron.command.Feedback;
import eu.vexiron.gui.IslandSelectionGUI;
import eu.vexiron.island.IslandManager;
import eu.vexiron.island.IslandProvider;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class IslandCommand extends Command {

    public IslandCommand(IslandProvider islands, IslandSelectionGUI selectionGUI) {
        super("island", "is");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player player)) {
                Feedback.error(sender, "Only players can use this command.");
                return;
            }

            IslandManager manager = islands.manager();

            manager.getIsland(player.getUuid()).thenAccept(optional -> {
                if (optional.isEmpty()) {
                    // No island → open selector
                    selectionGUI.open(player);
                    return;
                }

                manager.teleportTo(player, optional.get()).thenRun(() ->
                        Feedback.success(player, "Teleported to your island.")
                );
            });
        });
    }
}