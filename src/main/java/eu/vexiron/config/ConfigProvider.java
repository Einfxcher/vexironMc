package eu.vexiron.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProvider.class);
    private static final Path FILE = Path.of("config.yml");

    private final Config config;

    public ConfigProvider() {
        this.config = load();
    }

    public Config get() {
        return config;
    }

    private Config load() {
        if (!Files.exists(FILE)) {
            LOGGER.info("No config found — creating default at {}", FILE);
            Config defaults = new Config();
            save(defaults);
            return defaults;
        }

        try (Reader reader = Files.newBufferedReader(FILE)) {
            Yaml yaml = new Yaml(new Constructor(Config.class, new org.yaml.snakeyaml.LoaderOptions()));
            Config loaded = yaml.load(reader);
            LOGGER.info("Loaded config from {}", FILE);
            return loaded != null ? loaded : new Config();
        } catch (IOException e) {
            LOGGER.error("Failed to load config, using defaults", e);
            return new Config();
        }
    }

    public void save(Config config) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);

        Representer representer = new Representer(options);
        representer.addClassTag(Config.class, org.yaml.snakeyaml.nodes.Tag.MAP);

        Yaml yaml = new Yaml(representer, options);

        try (Writer writer = Files.newBufferedWriter(FILE)) {
            yaml.dump(config, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }
}