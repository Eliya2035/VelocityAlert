package me.Eliya.velocityalert.Utilities;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.*;
import java.util.Map;

public class ConfigProperties {
    private final Path configPath;
    private Map<String, Object> config;

    public ConfigProperties(Path configPath) {
        this.configPath = configPath;
    }

    // Load the configuration file or create a new one if it doesn't exist
    public void loadConfig() {
        try {
            // Check if config file exists
            if (!Files.exists(configPath)) {
                // Create directories for config file path if they don't exist
                Files.createDirectories(configPath.getParent());

                // Copy the default config from resources to the config path
                InputStream defaultConfig = getClass().getResourceAsStream("/config.yml");
                if (defaultConfig == null) {
                    throw new IllegalStateException("Default configuration file 'config.yml' is missing. Ensure it is included in the JAR.");
                }

                // Copy default configuration to the specified config path
                Files.copy(defaultConfig, configPath);
                System.out.println("Default configuration file created at: " + configPath.toAbsolutePath());
            }

            // Load the configuration from file
            try (InputStream inputStream = Files.newInputStream(configPath)) {
                Yaml yaml = createYaml();
                config = yaml.load(inputStream);
                System.out.println("Configuration file loaded successfully.");
            }
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    // Create a Yaml instance with custom settings for dumping and loading
    private Yaml createYaml() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);  // Nice block-style YAML
        return new Yaml(options);
    }

    // Get a value from the configuration by key
    public Object get(String key) {
        return config.get(key);
    }

    // Set a value in the configuration for a given key
    public void set(String key, Object value) {
        config.put(key, value);
    }

    // Save the configuration back to the file
    public void saveConfig() {
        try (OutputStream outputStream = Files.newOutputStream(configPath);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {

            Yaml yaml = createYaml();
            yaml.dump(config, writer);
            System.out.println("Configuration file saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reload the configuration (useful after modifying it)
    public void reloadConfig() {
        loadConfig();
    }

    // Get the entire configuration as a Map
    public Map<String, Object> getConfig() {
        return config;
    }
}
