package eu.vexiron.rank;

import eu.vexiron.database.Database;

public final class RankProvider {

    private final RankManager manager;

    public RankProvider(Database database) {
        RankStorage storage = new PostgresRankStorage(database);
        this.manager = new RankManager(storage);
        new RankListener(manager);
    }

    public RankManager manager() {
        return manager;
    }
}