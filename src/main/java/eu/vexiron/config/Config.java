package eu.vexiron.config;

public final class Config {

    public String host = "0.0.0.0";
    public int port = 25565;
    public String brand = "Vexiron";
    public int maxPlayers = 200;

    public Database database = new Database();

    public boolean dataSaving = true;

    public static final class Database {
        public String type = "sqlite";
        public String host = "localhost";
        public int port = 3306;
        public String name = "vexiron";
        public String user = "vexiron";
        public String password = "";
        public String file = "data/vexiron.db";
    }
}