package me.mstn.tools;

import me.mstn.tools.command.MaintenanceCommand;
import me.mstn.tools.common.configuration.SimpleConfiguration;
import me.mstn.tools.common.Maintenance;
import me.mstn.tools.common.util.ProtocolUtil;
import me.mstn.tools.listener.PluginEvents;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;

public class MasstonToolsPlugin extends Plugin {

    public static File PATH;

    private static SimpleConfiguration simpleConfiguration;
    private static Configuration configuration;

    public void onEnable() {
        PATH = getDataFolder();

        simpleConfiguration = new SimpleConfiguration(this);
        simpleConfiguration.saveDefaultConfig();

        simpleConfiguration.saveResource("outdated.png");
        simpleConfiguration.saveResource("maintenance.png");

        configuration = simpleConfiguration.getConfig();

        Maintenance.setAdmins(configuration.getStringList("maintenance.admin-list"));
        Maintenance.setEnabled(configuration.getBoolean("maintenance.enabled"));

        getProxy().getPluginManager().registerListener(this, new PluginEvents());
        getProxy().getPluginManager().registerCommand(this, new MaintenanceCommand());

        if (!ProtocolUtil.checkValidVersion(configuration.getString("outdated.whitelist"))) {
            getLogger().severe("The version in the configuration is incorrect!");
        }
    }

    public void onDisable() {
        getProxy().getPluginManager().unregisterListeners(this);
        getProxy().getPluginManager().unregisterCommands(this);
    }

    public static SimpleConfiguration getConfigurationManipulator() {
        return simpleConfiguration;
    }

    public static Configuration getConfig() {
        return configuration;
    }

}
