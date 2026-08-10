package eu.vexiron;

import eu.vexiron.command.CommandProvider;
import eu.vexiron.config.Config;
import eu.vexiron.config.ConfigProvider;
import eu.vexiron.database.DatabaseProvider;
import eu.vexiron.listener.ListenerProvider;
import eu.vexiron.profile.ProfileProvider;
import eu.vexiron.rank.RankProvider;
import eu.vexiron.server.Motd;
import eu.vexiron.server.Settings;
import eu.vexiron.world.InstanceProvider;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;

public final class Main {

    static void main() {
        ConfigProvider configProvider = new ConfigProvider();
        Config config = configProvider.get();

        MinecraftServer server = MinecraftServer.init(new Auth.Online());
        MinecraftServer.setBrandName(config.brand);

        Settings.setDataSaving(config.dataSaving);

        DatabaseProvider database = new DatabaseProvider(config);
        InstanceProvider instances = new InstanceProvider();
        RankProvider ranks = new RankProvider(database.get());
        ProfileProvider profiles = new ProfileProvider(database.get());

        new ListenerProvider(instances, ranks);
        new CommandProvider(instances, ranks, profiles);
        new Motd(config);

        server.start(config.host, config.port);
    }
}