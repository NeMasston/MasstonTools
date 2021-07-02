package me.mstn.tools.common.util;

import net.md_5.bungee.protocol.ProtocolConstants;

import java.lang.reflect.Field;

public class ProtocolUtil {

    public static int getVersionId(String version) {
        int versionId = 0;

        try {
            Field field = ProtocolConstants.class.getDeclaredField(version);
            versionId = field.getInt(field);
        } catch (Exception e) {
            return 0;
        }

        return versionId;
    }

    public static boolean checkValidVersion(String version) {
        try {
            ProtocolConstants.class.getDeclaredField(version);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
