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

import java.awt.image.BufferedImage;

public class PluginEvents implements Listener {

    private final Configuration configuration;
    private final BufferedImage oldVersionImage;
    private final BufferedImage maintenanceImage;

    public PluginEvents() {
        configuration = Main.getConfig();
        oldVersionImage = ImageUtil.loadImage(configuration.getString("old_versions.icon.name"));
        maintenanceImage = ImageUtil.loadImage(configuration.getString("maintenance.icon.name"));
    }

    @EventHandler
    public void on(PreLoginEvent e) {
        if (configuration.getBoolean("old_versions.enabled")) {
            int version = e.getConnection().getVersion();

            if (version < ProtocolUtil.getVersionId(configuration.getString("old_versions.whitelist"))) {
                e.setCancelReason(new TextComponent(ChatColor.translateAlternateColorCodes(
                        '&', configuration.getString("old_versions.kick-reason")
                )));
                e.setCancelled(true);
            }
        }

        if (Maintenance.isEnabled()) {
            if (!Maintenance.find(e.getConnection().getName())) {
                e.setCancelReason(new TextComponent(ChatColor.translateAlternateColorCodes(
                        '&', configuration.getString("maintenance.kick-reason")
                )));
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void on(ProxyPingEvent e) {
        ServerPing ping = e.getResponse();
        ServerPing.Protocol vers = ping.getVersion();

        if (configuration.getBoolean("old_versions.enabled")) {
            int version = e.getConnection().getVersion();

            if (version < ProtocolUtil.getVersionId(configuration.getString("old_versions.whitelist"))) {
                vers.setName(ChatColor.translateAlternateColorCodes(
                        '&', configuration.getString("old_versions.error")
                ));
                vers.setProtocol(-1);
                ping.setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes(
                        '&', configuration.getString("old_versions.motd.first") + "\n"
                                + configuration.getString("old_versions.motd.second")
                )));

                if (configuration.getBoolean("old_versions.icon.enabled") && oldVersionImage != null) {
                    if (oldVersionImage.getWidth() != 64 || oldVersionImage.getHeight() != 64) {
                        throw new RuntimeException("Не удалось установить картинку \"old_version.png\", она должна иметь размер 64x64!");
                    } else {
                        ping.setFavicon(Favicon.create(oldVersionImage));
                    }
                }

                e.setResponse(ping);
            }
        }

        if (Maintenance.isEnabled()) {
            vers.setName(ChatColor.translateAlternateColorCodes(
                    '&', configuration.getString("maintenance.error")
            ));
            vers.setProtocol(-1);
            ping.setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes(
                    '&', configuration.getString("maintenance.motd.first") + "\n"
                            + configuration.getString("maintenance.motd.second")
            )));

            if (configuration.getBoolean("maintenance.icon.enabled") && maintenanceImage != null) {
                if (maintenanceImage.getWidth() != 64 || maintenanceImage.getHeight() != 64) {
                    throw new RuntimeException("Не удалось установить картинку \"maintenance.png\", она должна иметь размер 64x64!");
                } else {
                    ping.setFavicon(Favicon.create(maintenanceImage));
                }
            }

            e.setResponse(ping);
        }
    }

}
