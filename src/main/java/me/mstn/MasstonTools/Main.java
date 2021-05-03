package me.mstn.MasstonTools;

import me.mstn.MasstonTools.configuration.SimpleConfiguration;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class Main extends Plugin {

    private SimpleConfiguration simpleConfiguration;
    private static Configuration configuration;

    public void onEnable() {
        simpleConfiguration = new SimpleConfiguration(this);
        configuration = simpleConfiguration.getConfig();
    }

    public void onDisable() {

    }

    public static Configuration getConfig() {
        return configuration;
    }

}
