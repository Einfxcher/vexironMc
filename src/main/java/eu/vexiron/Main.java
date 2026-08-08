package eu.vexiron;

import eu.vexiron.command.CommandProvider;
import eu.vexiron.listener.ListenerProvider;
import eu.vexiron.server.Motd;
import eu.vexiron.server.Settings;
import eu.vexiron.world.InstanceProvider;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;

public final class Main {
    static void main() {
        MinecraftServer server = MinecraftServer.init(new Auth.Online());
        MinecraftServer.setBrandName("Vexiron");

        Settings.setDataSaving(false);

        InstanceProvider instances = new InstanceProvider();

        new ListenerProvider(instances);
        new CommandProvider(instances);
        new Motd();

        server.start("0.0.0.0", 25565);
    }
}