package eu.vexiron.rank;

import java.nio.file.Path;

public final class RankProvider {

    private final RankManager manager;
    private final RankFileWatcher watcher;

    public RankProvider() {
        Path file = Path.of("data", "ranks.json");
        RankStorage storage = new JsonRankStorage(file);
        this.manager = new RankManager(storage);

        this.watcher = new RankFileWatcher(manager, file);
        this.watcher.start();

        new RankListener(manager);
    }

    public RankManager manager() {
        return manager;
    }
}