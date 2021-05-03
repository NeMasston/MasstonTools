package me.mstn.MasstonTools;

import me.mstn.MasstonTools.command.MaintenanceCommand;
import me.mstn.MasstonTools.core.configuration.SimpleConfiguration;
import me.mstn.MasstonTools.core.Maintenance;
import me.mstn.MasstonTools.core.util.ProtocolUtil;
import me.mstn.MasstonTools.listener.PluginEvents;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;

public class Main extends Plugin {

    public static File PATH;

    private static SimpleConfiguration simpleConfiguration;
    private static Configuration configuration;

    public void onEnable() {
        PATH = getDataFolder();

        simpleConfiguration = new SimpleConfiguration(this);
        simpleConfiguration.saveDefaultConfig();

        simpleConfiguration.saveResource("old_version.png");
        simpleConfiguration.saveResource("maintenance.png");

        configuration = simpleConfiguration.getConfig();

        Maintenance.setAdmins(configuration.getStringList("maintenance.admin-list"));
        Maintenance.setEnabled(configuration.getBoolean("maintenance.enabled"));

        getProxy().getPluginManager().registerListener(this, new PluginEvents());
        getProxy().getPluginManager().registerCommand(this, new MaintenanceCommand());

        if (!ProtocolUtil.checkValidVersion(configuration.getString("maintenance.whitelist"))) {
            getLogger().severe("Версия в конфигурации указана неверно!");
        }
    }

    public void onDisable() {

    }

    public static SimpleConfiguration getConfigurationManipulator() {
        return simpleConfiguration;
    }

    public static Configuration getConfig() {
        return configuration;
    }

}
