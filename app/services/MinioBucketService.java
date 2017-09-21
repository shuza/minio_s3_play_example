package services;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import configs.ParamsConfig;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.xmlpull.v1.XmlPullParserException;
import play.Logger;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MinioBucketService {
    private String TAG = this.getClass().getSimpleName();

    private MinioConnectionService connectionService;

    public MinioBucketService() {
        connectionService = new MinioConnectionService();
    }

    public JsonObject create(String bucketName) {
        JsonObject result = new JsonObject();
        try {
            if (!connectionService.getMinioClient().bucketExists(bucketName)) {
                connectionService.getMinioClient().makeBucket(bucketName);
                result.add("message", "Bucket " + bucketName + " has been created.");
            } else {
                result.add("message", "Bucket " + bucketName + " exists.");
            }
            result.add("code", 200);
            result.add("response", "success");
        } catch (RegionConflictException e) {
            e.printStackTrace();
            result.add("code", ParamsConfig.RESULT_SUCCESS);
            result.add("response", "failed");
            result.add("message", e.getMessage());
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
                | InvalidKeyException | ErrorResponseException | IOException
                | NoResponseException | InternalException | XmlPullParserException e) {
            e.printStackTrace();
            Logger.debug(TAG, "  :  " + e.getMessage());
            result = appendDefaultErrorMessage(result);
            result.add("message", e.getMessage());
        }
        return result;
    }

    public JsonObject list() {
        JsonObject result = new JsonObject();
        try {
            JsonArray bucketAsJsonArray = new JsonArray();
            List<Bucket> bucketList = connectionService.getMinioClient().listBuckets();
            for (Bucket bucket : bucketList) {
                JsonObject bucketAsJson = new JsonObject();
                bucketAsJson.add("bucket_name", bucket.name());
                bucketAsJson.add("bucket_create_date", bucket.creationDate().getTime());
                bucketAsJsonArray.add(bucketAsJson);
            }

            result.add("code", ParamsConfig.RESULT_SUCCESS);
            result.add("response", "success");
            result.add("data", bucketAsJsonArray);
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
                | InvalidKeyException | ErrorResponseException | IOException
                | NoResponseException | InternalException | XmlPullParserException e) {
            e.printStackTrace();
            Logger.debug(TAG, "  :  " + e.getMessage());
            result = appendDefaultErrorMessage(result);
            result.add("message", e.getMessage());
        }
        return result;
    }

    public JsonObject remove(String bucketName) {
        JsonObject result = new JsonObject();
        try {
            connectionService.getMinioClient().removeBucket(bucketName);

            result.add("code", ParamsConfig.RESULT_SUCCESS);
            result.add("response", "success");
            result.add("message", "Bucket " + bucketName + " has been removed.");
            result.add("data", true);
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
                | InvalidKeyException | ErrorResponseException | IOException
                | NoResponseException | InternalException | XmlPullParserException e) {
            e.printStackTrace();
            Logger.debug(TAG, "  :  " + e.getMessage());
            result = appendDefaultErrorMessage(result);
            result.add("message", e.getMessage());
        }
        return result;
    }

    public JsonObject check(String bucketName) {
        JsonObject result = new JsonObject();
        try {
            if (connectionService.getMinioClient().bucketExists(bucketName)) {
                result.add("message", "Bucket " + bucketName + " exists.");
                result.add("data", true);
            } else {
                result.add("message", "Bucket " + bucketName + " doesn't exists.");
                result.add("data", false);
            }
            result.add("code", ParamsConfig.RESULT_SUCCESS);
            result.add("response", "success");
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
                | InvalidKeyException | ErrorResponseException | IOException
                | NoResponseException | InternalException | XmlPullParserException e) {
            e.printStackTrace();
            Logger.debug(TAG, "  :  " + e.getMessage());
            result = appendDefaultErrorMessage(result);
            result.add("message", e.getMessage());
        }
        return result;
    }

    public JsonObject appendDefaultErrorMessage(JsonObject result) {
        result.add("code", 201);
        result.add("response", "failed");
        return result;
    }
}
