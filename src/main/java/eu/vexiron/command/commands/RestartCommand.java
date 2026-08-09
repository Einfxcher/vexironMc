package eu.vexiron.command.commands;

import eu.vexiron.command.Feedback;
import eu.vexiron.server.Settings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;

public class RestartCommand extends Command {

    public RestartCommand() {
        super("restart");

        setDefaultExecutor((sender, _) -> {
            Feedback.success(sender, "Server restarting in 5 seconds...");

            for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                player.sendActionBar(Component.text("Server restarting...", NamedTextColor.RED));
            }

            MinecraftServer.getSchedulerManager().scheduleTask(() -> {

                if (Settings.isDataSaving()) {
                    saveAll();
                }

                for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    player.kick(Component.text()
                            .append(Component.text("Server is restarting.", NamedTextColor.RED))
                            .append(Component.newline())
                            .append(Component.text("Please reconnect shortly.", NamedTextColor.GRAY))
                            .build());
                }
            }, TaskSchedule.seconds(3), TaskSchedule.stop());

            MinecraftServer.getSchedulerManager().scheduleTask(MinecraftServer::stopCleanly, TaskSchedule.seconds(5), TaskSchedule.stop());
        });
    }

    private void saveAll() {
        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            instance.saveChunksToStorage();
        }

        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            savePlayer(player);
        }
    }

    private void savePlayer(Player player) {
        // TODO: PlayerData Saving (Profile)
    }
}