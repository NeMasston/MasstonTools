package me.mstn.tools.common;

import me.mstn.tools.MasstonToolsPlugin;

import java.util.ArrayList;
import java.util.List;

public class Maintenance {

    private static List<String> admins = new ArrayList<>();
    private static boolean enabled = false;

    public static void setEnabled(boolean to) {
        enabled = to;
        saveStatus();
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static boolean find(String name) {
        return admins.contains(name);
    }

    public static boolean addAdmin(String name) {
        if (!find(name)) {
            admins.add(name);
            saveList();

            return true;
        }

        return false;
    }

    public static boolean removeAdmin(String name) {
        if (find(name)) {
            admins.remove(name);
            saveList();

            return true;
        }

        return false;
    }

    public static List<String> getAdmins() {
        return admins;
    }

    public static void setAdmins(List<String> list) {
        admins = list;
    }

    private static void saveStatus() {
        MasstonToolsPlugin.getConfig().set("maintenance.enabled", enabled);
        MasstonToolsPlugin.getConfigurationManipulator().save();
    }

    private static void saveList() {
        MasstonToolsPlugin.getConfig().set("maintenance.admin-list", admins);
        MasstonToolsPlugin.getConfigurationManipulator().save();
    }

}
