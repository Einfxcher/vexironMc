package eu.vexiron.island;

import eu.vexiron.database.Database;

public final class IslandProvider {

    private final IslandManager manager;

    public IslandProvider(Database database) {
        IslandRepository repository = new IslandRepository(database);
        this.manager = new IslandManager(repository);
        new IslandListener(manager);
    }

    public IslandManager manager() {
        return manager;
    }
}