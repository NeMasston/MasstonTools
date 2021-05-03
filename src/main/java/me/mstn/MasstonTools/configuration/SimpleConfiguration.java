package me.mstn.MasstonTools.configuration;

import com.google.common.io.ByteStreams;
import me.mstn.MasstonTools.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class SimpleConfiguration {

    private final String fileName = "config.yml";

    private final Main plugin;
    private Configuration configuration;

    public SimpleConfiguration(Main plugin) {
        this.plugin = plugin;

        make();
        load();
    }

    public Configuration getConfig() {
        return configuration;
    }

    public void save() {
        try {
            ConfigurationProvider
                    .getProvider(YamlConfiguration.class)
                    .save(configuration, new File(plugin.getDataFolder(), fileName));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить конфигурационный файл", e);
        }
    }

    private void make() {
        File directory = plugin.getDataFolder();
        if (!directory.exists()) directory.mkdir();
    }

    private void load() {
        File file = new File(plugin.getDataFolder(), fileName);

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
                throw new RuntimeException("Не удалось загрузить конфигурационный файл", e);
            }
        }
    }

}
