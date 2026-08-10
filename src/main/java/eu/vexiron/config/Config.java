package eu.vexiron.config;

public final class Config {

    // Server
    public String host = "0.0.0.0";
    public int port = 25565;
    public String brand = "Vexiron";
    public int maxPlayers = 200;

    // Database
    public Database database = new Database();

    // Development
    public boolean dataSaving = true;

    public static final class Database {
        public String host = "localhost";
        public int port = 5432;
        public String name = "vexiron";
        public String user = "vexiron";
        public String password = "changeme";
    }
}