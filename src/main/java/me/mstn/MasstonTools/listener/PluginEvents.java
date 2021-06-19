package me.mstn.MasstonTools.listener;

import me.mstn.MasstonTools.Main;
import me.mstn.MasstonTools.core.Maintenance;
import me.mstn.MasstonTools.core.util.ImageUtil;
import me.mstn.MasstonTools.core.util.ProtocolUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.awt.image.BufferedImage;

public class PluginEvents implements Listener {

    private final Configuration configuration;

    private final Configuration oldVersions;
    private final Configuration motdSectionOldVersions;
    private final Configuration iconSectionOldVersions;

    private final Configuration maintenance;
    private final Configuration motdSectionMaintenance;
    private final Configuration iconSectionMaintenance;

    private final BufferedImage oldVersionImage;
    private final BufferedImage maintenanceImage;

    public PluginEvents() {
        configuration = Main.getConfig();

        oldVersions = configuration.getSection("old_versions");
        motdSectionOldVersions = oldVersions.getSection("motd");
        iconSectionOldVersions = oldVersions.getSection("icon");

        maintenance = configuration.getSection("maintenance");
        motdSectionMaintenance = maintenance.getSection("motd");
        iconSectionMaintenance = maintenance.getSection("icon");

        oldVersionImage = ImageUtil.loadImage(iconSectionOldVersions.getString("name"));
        maintenanceImage = ImageUtil.loadImage(iconSectionMaintenance.getString("name"));
    }

    @EventHandler
    public void on(PreLoginEvent e) {
        String name = e.getConnection().getName();

        if (oldVersions.getBoolean("enabled")) {
            int version = e.getConnection().getVersion();

            if (version < ProtocolUtil.getVersionId(oldVersions.getString("whitelist"))) {
                e.setCancelReason(new TextComponent(ChatColor.translateAlternateColorCodes(
                        '&', oldVersions.getString("kick-reason")
                                .replace("{player}", name)
                                .replace("{newline}", "\n")
                )));
                e.setCancelled(true);
            }
        }

        if (Maintenance.isEnabled()) {
            if (!Maintenance.find(name)) {
                e.setCancelReason(new TextComponent(ChatColor.translateAlternateColorCodes(
                        '&', maintenance.getString("kick-reason")
                                .replace("{player}", name)
                                .replace("{newline}", "\n")
                )));
                e.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(ProxyPingEvent e) {
        ServerPing ping = e.getResponse();
        ServerPing.Protocol vers = ping.getVersion();

        if (oldVersions.getBoolean("enabled")) {
            int version = e.getConnection().getVersion();

            if (version < ProtocolUtil.getVersionId(oldVersions.getString("whitelist"))) {
                vers.setName(ChatColor.translateAlternateColorCodes(
                        '&', oldVersions.getString("error")
                ));

                vers.setProtocol(-1);
                ping.setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes(
                        '&', motdSectionOldVersions.getString("first") + "\n"
                                + motdSectionOldVersions.getString("second")
                )));

                if (iconSectionOldVersions.getBoolean("enabled") && oldVersionImage != null) {
                    if (oldVersionImage.getWidth() != 64 || oldVersionImage.getHeight() != 64) {
                        throw new RuntimeException("Failed to install picture \""+ oldVersionImage +"\", it must be 64x64!");
                    } else {
                        ping.setFavicon(Favicon.create(oldVersionImage));
                    }
                }

                e.setResponse(ping);
            }
        }

        if (Maintenance.isEnabled()) {
            vers.setName(ChatColor.translateAlternateColorCodes(
                    '&', maintenance.getString("error")
            ));

            vers.setProtocol(-1);
            ping.setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes(
                    '&', motdSectionMaintenance.getString("first") + "\n"
                            + motdSectionMaintenance.getString("second")
            )));

            if (iconSectionMaintenance.getBoolean("enabled") && maintenanceImage != null) {
                if (maintenanceImage.getWidth() != 64 || maintenanceImage.getHeight() != 64) {
                    throw new RuntimeException("Failed to install picture \""+ maintenanceImage +"\", it must be 64x64!");
                } else {
                    ping.setFavicon(
                            Favicon.create(maintenanceImage)
                    );
                }
            }

            e.setResponse(ping);
        }
    }

}
