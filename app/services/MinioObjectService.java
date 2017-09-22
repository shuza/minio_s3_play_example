package services;

import com.eclipsesource.json.JsonObject;
import configs.MinioConfig;
import configs.ParamsConfig;
import controllers.BaseController;
import io.minio.errors.*;
import org.xmlpull.v1.XmlPullParserException;
import play.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioObjectService extends BaseController {
    private String TAG = this.getClass().getSimpleName();

    private MinioConnectionService connectionService;

    public MinioObjectService() {
        connectionService = MinioConnectionService.getInstance();
    }

    public JsonObject createObject(String bucketName, String objectName, InputStream inputStream, String contentType) {
        JsonObject result = new JsonObject();
        try {
            connectionService.getMinioClient().putObject(bucketName, objectName, inputStream, contentType);

            JsonObject data = new JsonObject();
            data.add("object_name", objectName);
            data.add("object_uri", MinioConfig.getMsHost()
                    + File.separator + "bucket" + File.separator + bucketName
                    + File.separator + "object" + File.separator + objectName);

            result.add("code", ParamsConfig.RESULT_SUCCESS);
            result.add("response", "success");
            result.add("messge", "Object " + objectName + " has been created.");
            result.add("data", data);
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException |
                InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException |
                InternalException | InvalidArgumentException e) {
            e.printStackTrace();
            Logger.debug(TAG + " : " + e.getMessage());

            result.add("code", 201);
            result.add("response", "failed");
            result.add("message", e.getMessage());
        }

        return result;
    }

    public InputStream getObject(String bucketName, String objectName) {
        try {
            return connectionService.getMinioClient().getObject(bucketName, objectName);
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException |
                InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException |
                InternalException | InvalidArgumentException e) {
            Logger.debug(TAG, "  :  " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
