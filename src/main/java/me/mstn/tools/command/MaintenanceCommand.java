package me.mstn.tools.command;

import me.mstn.tools.MasstonToolsPlugin;
import me.mstn.tools.common.Maintenance;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class MaintenanceCommand extends Command {

    private final Configuration configuration;

    public MaintenanceCommand() {
        super("mnt", null, "обслуживание", "maintenance", "техработы");
        configuration = MasstonToolsPlugin.getConfig();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Configuration maintenance = configuration.getSection("maintenance");
        Configuration messages = maintenance.getSection("messages");

        if (!commandSender.hasPermission("masstontools.use")) {
            commandSender.sendMessage(colored(messages.getString("permission-denied")));
            return;
        }

        if (args.length > 0) {
            if (args.length == 1) {
                String key = args[0].toLowerCase();

                if (key.equals("вкл") || key.equals("on") || key.equals("включить") || key.equals("true")) {
                    Maintenance.setEnabled(true);

                    commandSender.sendMessage(colored(messages.getString("enabled")));
                    return;
                } else if (key.equals("выкл") || key.equals("off") || key.equals("выключить") || key.equals("false")) {
                    Maintenance.setEnabled(false);

                    commandSender.sendMessage(colored(messages.getString("disabled")));
                    return;
                }
            }

            if (args.length == 2) {
                String key = args[0].toLowerCase();

                if (key.equals("add") || key.equals("добавить")) {
                    boolean addUser = Maintenance.addAdmin(args[1]);

                    if (!addUser) {
                        commandSender.sendMessage(colored(messages.getString("already-added")));
                        return;
                    }

                    commandSender.sendMessage(colored(messages.getString("added").replace("{player}", args[1])));
                    return;
                } else if (key.equals("remove") || key.equals("убрать") || key.equals("удалить")) {
                    boolean removeUser = Maintenance.removeAdmin(args[1]);

                    if (!removeUser) {
                        commandSender.sendMessage(colored(messages.getString("already-removed")));
                        return;
                    }

                    commandSender.sendMessage(colored(messages.getString("removed").replace("{player}", args[1])));
                    return;
                }
            }
        }

        for (String helpLine : messages.getStringList("help")) {
            commandSender.sendMessage(colored(helpLine));
        }

    }

    private TextComponent colored(String message) {
        return new TextComponent(
                ChatColor.translateAlternateColorCodes(
                        '&', message
                )
        );
    }

}
