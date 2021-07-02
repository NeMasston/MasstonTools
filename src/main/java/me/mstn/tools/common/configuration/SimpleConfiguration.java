package me.mstn.tools.common.configuration;

import com.google.common.io.ByteStreams;
import me.mstn.tools.MasstonToolsPlugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SimpleConfiguration {

    private final String fileName = "config.yml";

    private final MasstonToolsPlugin plugin;
    private Configuration configuration;

    public SimpleConfiguration(MasstonToolsPlugin plugin) {
        this.plugin = plugin;

        make();
        load();
    }

    public Configuration getConfig() {
        return configuration;
    }

    public void saveDefaultConfig() {
        saveResource(fileName);
        reload();
    }

    public void saveResource(String resourceName) {
        try {
            Path configPath = MasstonToolsPlugin.PATH.toPath().resolve(resourceName);

            if (Files.exists(configPath)) {
                return;
            }

            Files.copy(plugin.getResourceAsStream(resourceName), configPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            ConfigurationProvider
                    .getProvider(YamlConfiguration.class)
                    .save(configuration, new File(MasstonToolsPlugin.PATH, fileName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config file.", e);
        }
    }

    private void make() {
        File directory = MasstonToolsPlugin.PATH;
        if (!directory.exists()) directory.mkdir();
    }

    public void reload() {
        try {
            configuration = YamlConfiguration
                    .getProvider(YamlConfiguration.class)
                    .load(MasstonToolsPlugin.PATH.toPath().resolve(fileName).toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to reload config file.", e);
        }
    }

    private void load() {
        File file = new File(MasstonToolsPlugin.PATH, fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
                try (
                        InputStream is = plugin.getResourceAsStream(fileName);
                        OutputStream os = new FileOutputStream(file)
                ) {
                    ByteStreams.copy(is, os);
                }

                configuration = ConfigurationProvider
                        .getProvider(YamlConfiguration.class)
                        .load(file);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save config file.", e);
            }
        }
    }

}
