package me.mstn.MasstonTools.core;

import me.mstn.MasstonTools.Main;

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
        for (String element : admins) {
            if (name.equalsIgnoreCase(element)) {
                return true;
            }
        }

        return false;
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
        Main.getConfig().set("maintenance.enabled", enabled);
        Main.getConfigurationManipulator().save();
    }

    private static void saveList() {
        Main.getConfig().set("maintenance.admin-list", admins);
        Main.getConfigurationManipulator().save();
    }

}
