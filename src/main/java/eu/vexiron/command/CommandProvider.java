package eu.vexiron.command;

import eu.vexiron.command.commands.*;
import eu.vexiron.gui.IslandSelectionGUI;
import eu.vexiron.island.IslandProvider;
import eu.vexiron.profile.ProfileProvider;
import eu.vexiron.rank.RankProvider;
import eu.vexiron.world.InstanceProvider;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;

public class CommandProvider {

    public CommandProvider(InstanceProvider instances,
                           RankProvider ranks,
                           ProfileProvider profiles,
                           IslandProvider islands,
                           IslandSelectionGUI selectionGUI) {
        CommandManager manager = MinecraftServer.getCommandManager();

        manager.register(new StopCommand());
        manager.register(new RestartCommand());
        manager.register(new RankCommand(ranks.manager()));
        manager.register(new ProfileCommand(profiles.manager()));
        manager.register(new IslandCommand(islands, selectionGUI));
        manager.register(new HubCommand(instances));
    }
}