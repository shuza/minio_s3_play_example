package utils;

import models.Session;
import models.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import java.util.UUID;

public class CryptoUtil {
    public static String toMD5Hash(String value) {
        return DigestUtils.md2Hex(value);
    }

    public static String toSHA1Hash(String value) {
        return DigestUtils.sha1Hex(value);
    }

    public static String toSHA256Hash(String value) {
        return DigestUtils.sha256Hex(value);
    }

    public static String toSHA512Hash(String value) {
        return DigestUtils.sha512Hex(value);
    }

    public static String toPasswordHash(String value) {
        return toMD5Hash(toSHA256Hash(value));
    }

    public static String toSecuredToken() {
        return toSHA1Hash(UUID.randomUUID().toString());
    }

    public static Session getNewSession(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setSessionId(AppUtil.getUUID());
        session.setAccessToken(toSecuredToken());
        session.setRefreshToken(toSecuredToken());
        session.setSessionBegainTime(DateTime.now().toDate());
        session.setSessionEndTime(new DateTime(session.getSessionBegainTime()).plus(AppUtil.SESSION_LENGTH_IN_HOURS).toDate());
        return session;
    }
}
