package configs;

import play.Configuration;
import play.Environment;

import java.util.UUID;

public class MinioConfig {
    private static Configuration configuration = Configuration.load(Environment.simple());

    public static String getMsHost() {
        return configuration.getString("minioConfig.msHost");
    }

    public static String getMsUri() {
        return configuration.getString("minioConfig.msUri");
    }

    public static String getMsSecretKey() {
        return configuration.getString("minioConfig.secreteKey");
    }

    public static String getMsAccessKey() {
        return configuration.getString("minioConfig.msAccessToken");
    }

    public static String getRandomText() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
