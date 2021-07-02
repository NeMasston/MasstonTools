package me.mstn.tools.listener;

import me.mstn.tools.MasstonToolsPlugin;
import me.mstn.tools.common.Maintenance;
import me.mstn.tools.common.util.ImageUtil;
import me.mstn.tools.common.util.ProtocolUtil;
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
import java.util.UUID;

public class PluginEvents implements Listener {

    private final Configuration configuration;

    private final Configuration outdatedVersions;
    private final Configuration motdSectionOutdatedVersions;
    private final Configuration iconSectionOutdatedVersions;
    private final Configuration playersListSectionOutdatedVersions;

    private final Configuration maintenance;
    private final Configuration motdSectionMaintenance;
    private final Configuration iconSectionMaintenance;
    private final Configuration playersListSectionMaintenance;

    private final BufferedImage outdatedVersionImage;
    private final BufferedImage maintenanceImage;

    public PluginEvents() {
        configuration = MasstonToolsPlugin.getConfig();

        outdatedVersions = configuration.getSection("outdated");
        motdSectionOutdatedVersions = outdatedVersions.getSection("motd");
        iconSectionOutdatedVersions = outdatedVersions.getSection("icon");
        playersListSectionOutdatedVersions = outdatedVersions.getSection("playersList");

        maintenance = configuration.getSection("maintenance");
        motdSectionMaintenance = maintenance.getSection("motd");
        iconSectionMaintenance = maintenance.getSection("icon");
        playersListSectionMaintenance = maintenance.getSection("playersList");

        outdatedVersionImage = ImageUtil.loadImage(iconSectionOutdatedVersions.getString("name"));
        maintenanceImage = ImageUtil.loadImage(iconSectionMaintenance.getString("name"));
    }

    @EventHandler
    public void on(PreLoginEvent e) {
        String name = e.getConnection().getName();

        if (outdatedVersions.getBoolean("enabled")) {
            int version = e.getConnection().getVersion();

            if (version < ProtocolUtil.getVersionId(outdatedVersions.getString("whitelist"))) {
                e.setCancelReason(colored(outdatedVersions.getString("kick-reason")
                        .replace("{player}", name)
                        .replace("{newline}", "\n")));
                e.setCancelled(true);
            }
        }

        if (Maintenance.isEnabled()) {
            if (!Maintenance.find(name)) {
                e.setCancelReason(colored(maintenance.getString("kick-reason")
                        .replace("{player}", name)
                        .replace("{newline}", "\n")));
                e.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(ProxyPingEvent e) {
        ServerPing ping = e.getResponse();
        ServerPing.Protocol vers = ping.getVersion();
        ServerPing.Players players = ping.getPlayers();

        if (outdatedVersions.getBoolean("enabled")) {
            int version = e.getConnection().getVersion();

            if (version < ProtocolUtil.getVersionId(outdatedVersions.getString("whitelist"))) {
                vers.setName(ChatColor.translateAlternateColorCodes(
                        '&', outdatedVersions.getString("error")
                ));

                vers.setProtocol(-1);
                ping.setDescriptionComponent(colored(motdSectionOutdatedVersions.getString("first") + "\n" + motdSectionOutdatedVersions.getString("second")));

                if (iconSectionOutdatedVersions.getBoolean("enabled") && outdatedVersionImage != null) {
                    if (outdatedVersionImage.getWidth() != 64 || outdatedVersionImage.getHeight() != 64) {
                        throw new RuntimeException("Failed to install picture \""+ outdatedVersionImage +"\", it must be 64x64!");
                    } else {
                        ping.setFavicon(Favicon.create(outdatedVersionImage));
                    }
                }

                if (playersListSectionOutdatedVersions.getBoolean("enabled")) {
                    ServerPing.PlayerInfo[] playerInfo = playersListSectionOutdatedVersions
                            .getStringList("text")
                            .stream()
                            .map(line -> new ServerPing.PlayerInfo(
                                    ChatColor.translateAlternateColorCodes('&', line),
                                    UUID.randomUUID()
                            ))
                            .toArray(ServerPing.PlayerInfo[]::new);
                    players.setSample(playerInfo);
                    ping.setPlayers(players);
                }

                e.setResponse(ping);
            }
        }

        if (Maintenance.isEnabled()) {
            vers.setName(ChatColor.translateAlternateColorCodes(
                    '&', maintenance.getString("error")
            ));

            vers.setProtocol(-1);
            ping.setDescriptionComponent(colored(motdSectionMaintenance.getString("first") + "\n" + motdSectionMaintenance.getString("second")));

            if (iconSectionMaintenance.getBoolean("enabled") && maintenanceImage != null) {
                if (maintenanceImage.getWidth() != 64 || maintenanceImage.getHeight() != 64) {
                    throw new RuntimeException("Failed to install picture \""+ maintenanceImage +"\", it must be 64x64!");
                } else {
                    ping.setFavicon(
                            Favicon.create(maintenanceImage)
                    );
                }
            }

            if (playersListSectionMaintenance.getBoolean("enabled")) {
                ServerPing.PlayerInfo[] playerInfo = playersListSectionMaintenance
                        .getStringList("text")
                        .stream()
                        .map(line -> new ServerPing.PlayerInfo(
                                ChatColor.translateAlternateColorCodes('&', line),
                                UUID.randomUUID()
                        ))
                        .toArray(ServerPing.PlayerInfo[]::new);
                players.setSample(playerInfo);
                ping.setPlayers(players);
            }

            e.setResponse(ping);
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
