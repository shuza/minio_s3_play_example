package utils;

import play.Logger;

import java.util.UUID;

public class AppUtil {

    public static int SESSION_LENGTH_IN_HOURS = 24;

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void logDebug(Object scope, String message) {
        Logger.debug(scope.getClass().getSimpleName() + "  :==  " + message);
    }

    public static void logInfo(Object scope, String message) {
        Logger.info(scope.getClass().getSimpleName() + "  :==  " + message);
    }

    public static void logError(Object scope, String message) {
        Logger.error(scope.getClass().getSimpleName() + "  :==  " + message);
    }

}
