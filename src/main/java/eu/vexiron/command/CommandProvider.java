package eu.vexiron.command;

import eu.vexiron.command.commands.HubCommand;
import eu.vexiron.command.commands.RestartCommand;
import eu.vexiron.command.commands.StopCommand;
import eu.vexiron.world.InstanceProvider;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;

public class CommandProvider {

    public CommandProvider(InstanceProvider instances) {
        CommandManager manager = MinecraftServer.getCommandManager();

        manager.register(new HubCommand(instances));
        manager.register(new StopCommand());
        manager.register(new RestartCommand());
    }
}