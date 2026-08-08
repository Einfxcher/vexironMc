package eu.vexiron.server;

public class ServerConfigurer {

    private final Settings settings;

    public ServerConfigurer() {
        this.settings = new Settings();
        new Motd();
        new TabList();
    }

    public Settings settings() {
        return settings;
    }
}