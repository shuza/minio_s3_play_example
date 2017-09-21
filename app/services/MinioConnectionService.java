package services;

import configs.MinioConfig;
import io.minio.MinioClient;
import play.Logger;

public class MinioConnectionService {
    private String TAG = this.getClass().getSimpleName();

    private static MinioConnectionService minioConnectionService;
    private MinioClient minioClient;

    public static MinioConnectionService getInstance() {
        if (minioConnectionService == null) {
            minioConnectionService = new MinioConnectionService();
        }
        return minioConnectionService;
    }

    public MinioConnectionService() {
        initMinioClient();
    }

    private void initMinioClient() {
        try {
            minioClient = new MinioClient(MinioConfig.getMsUri(),
                    MinioConfig.getMsAccessKey(), MinioConfig.getMsSecretKey());
        } catch (Exception e) {
            e.printStackTrace();
            Logger.debug(TAG, "  :  " + e.getMessage());
        }
    }

    public MinioClient getMinioClient() {
        return minioClient;
    }
}
