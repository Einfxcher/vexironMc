package eu.vexiron.command.commands;

import eu.vexiron.command.Feedback;
import eu.vexiron.server.Settings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", "shutdown");


        setDefaultExecutor((sender, context) -> {
            Feedback.success(sender, "Server stopping in 5 seconds...");

            for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                player.sendActionBar(Component.text("Server shutting down...", NamedTextColor.RED));
            }

            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                if (Settings.isDataSaving()) {
                    saveAll();
                }

                for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                    player.kick(Component.text()
                            .append(Component.text("Server has shut down.", NamedTextColor.RED))
                            .append(Component.newline())
                            .append(Component.text("Thanks for playing!", NamedTextColor.GRAY))
                            .append(Component.newline())
                            .append(Component.text("More infos in our Discord soon!", NamedTextColor.GRAY))
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