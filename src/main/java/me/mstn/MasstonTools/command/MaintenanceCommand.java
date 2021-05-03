package me.mstn.MasstonTools.command;

import me.mstn.MasstonTools.Main;
import me.mstn.MasstonTools.core.Maintenance;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class MaintenanceCommand extends Command {

    private final Configuration configuration;

    public MaintenanceCommand() {
        super("mnt", null, "обслуживание", "maintenance", "техработы");
        configuration = Main.getConfig();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("masstontools.use")) {
            commandSender.sendMessage(new TextComponent(
                    ChatColor.translateAlternateColorCodes(
                            '&', configuration.getString("maintenance.messages.permission-denied")
                    )
            ));

            return;
        }

        if (args.length > 0) {
            if (args.length == 1) {
                String key = args[0].toLowerCase();

                if (key.equals("вкл") || key.equals("on") || key.equals("включить") || key.equals("true")) {
                    Maintenance.setEnabled(true);

                    commandSender.sendMessage(new TextComponent(
                            ChatColor.translateAlternateColorCodes(
                                    '&', configuration.getString("maintenance.messages.enabled")
                            )
                    ));

                    return;
                } else if (key.equals("выкл") || key.equals("off") || key.equals("выключить") || key.equals("false")) {
                    Maintenance.setEnabled(false);

                    commandSender.sendMessage(new TextComponent(
                            ChatColor.translateAlternateColorCodes(
                                    '&', configuration.getString("maintenance.messages.disabled")
                            )
                    ));

                    return;
                }
            }

            if (args.length == 2) {
                String key = args[0].toLowerCase();

                if (key.equals("add") || key.equals("добавить")) {
                    boolean addUser = Maintenance.addAdmin(args[1]);

                    if (!addUser) {
                        commandSender.sendMessage(new TextComponent(
                                ChatColor.translateAlternateColorCodes(
                                        '&', configuration.getString("maintenance.messages.already-added")
                                )
                        ));

                        return;
                    }

                    commandSender.sendMessage(new TextComponent(
                            ChatColor.translateAlternateColorCodes(
                                    '&', configuration.getString("maintenance.messages.added").replace("{player}", args[1])
                            )
                    ));

                    return;
                } else if (key.equals("remove") || key.equals("убрать") || key.equals("удалить")) {
                    boolean removeUser = Maintenance.removeAdmin(args[1]);

                    if (!removeUser) {
                        commandSender.sendMessage(new TextComponent(
                                ChatColor.translateAlternateColorCodes(
                                        '&', configuration.getString("maintenance.messages.already-removed")
                                )
                        ));

                        return;
                    }

                    commandSender.sendMessage(new TextComponent(
                            ChatColor.translateAlternateColorCodes(
                                    '&', configuration.getString("maintenance.messages.removed").replace("{player}", args[1])
                            )
                    ));

                    return;
                }
            }
        }

        for (String helpLine : configuration.getStringList("maintenance.messages.help")) {
            commandSender.sendMessage(new TextComponent(
                    ChatColor.translateAlternateColorCodes(
                            '&', helpLine
                    )
            ));
        }

    }

}
