package eu.vexiron.rank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;

public final class RankFileWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankFileWatcher.class);

    private final RankManager manager;
    private final Path file;
    private Thread watchThread;
    private volatile boolean running = true;

    public RankFileWatcher(RankManager manager, Path file) {
        this.manager = manager;
        this.file = file;
    }

    public void start() {
        watchThread = new Thread(this::watch, "RankFileWatcher");
        watchThread.setDaemon(true);
        watchThread.start();
    }

    public void stop() {
        running = false;
        if (watchThread != null) watchThread.interrupt();
    }

    private void watch() {
        Path dir = file.getParent();
        if (dir == null) return;

        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

            while (running) {
                WatchKey key = watcher.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changed = (Path) event.context();
                    if (changed.getFileName().equals(file.getFileName())) {
                        manager.reload();
                    }
                }

                if (!key.reset()) break;
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Rank file watcher stopped", e);
        }
    }
}