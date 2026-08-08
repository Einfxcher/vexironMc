package eu.vexiron;

import eu.vexiron.listener.ListenerProvider;
import eu.vexiron.server.ServerConfigurer;
import eu.vexiron.world.InstanceProvider;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;

public final class Main {

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init(new Auth.Online());
        MinecraftServer.setBrandName("Vexiron");

        InstanceProvider instances = new InstanceProvider();
        ServerConfigurer serverConfigurer = new ServerConfigurer();

        serverConfigurer.settings().setDataSaving(false);

        new ListenerProvider(instances);

        server.start("0.0.0.0", 25565);
    }
}