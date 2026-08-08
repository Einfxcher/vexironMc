package eu.vexiron.command.commands;

import eu.vexiron.command.Feedback;
import eu.vexiron.world.InstanceProvider;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class HubCommand extends Command {

    public HubCommand(InstanceProvider instances) {
        super("hub");

        setDefaultExecutor((sender, _) -> {
            if (!(sender instanceof Player player)) {
                Feedback.error(sender, "Only players can use this command.");
                return;
            }

            player.teleport(InstanceProvider.HUB_SPAWN);
            Feedback.success(player, "Teleported to the hub.");
        });
    }
}