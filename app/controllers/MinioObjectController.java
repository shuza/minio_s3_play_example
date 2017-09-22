package controllers;

import com.eclipsesource.json.JsonObject;
import configs.MinioConfig;
import play.Logger;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import security.DataValidation;
import services.MinioObjectService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MinioObjectController extends BaseController {
    private String TAG = this.getClass().getSimpleName();
    private MinioObjectService minioObjectService = new MinioObjectService();

    public Result create(String bucketName) {
        try {
            Http.MultipartFormData<File> requestedData = request().body().asMultipartFormData();
            for (Http.MultipartFormData.FilePart<File> requestedFile : requestedData.getFiles()) {
                String objectName = MinioConfig.getRandomText() + "-" + requestedFile.getFilename();
                FileInputStream objectInputStream = new FileInputStream(requestedFile.getFile());
                String objectContentType = requestedFile.getContentType();

                JsonObject result = minioObjectService.createObject(bucketName, objectName,
                        objectInputStream, objectContentType);
                return ok(Json.parse(result.toString()));
            }

        } catch (Exception e) {
            Logger.debug(TAG, "  :  " + e.getMessage());
            e.printStackTrace();
        }
        return ok(Json.parse(getUnknownErrorResult().toString()));
    }

    public Result getObject(String bucketName, String objectName) {
        try {
            InputStream inputStream = minioObjectService.getObject(bucketName, objectName);
            if (DataValidation.isNotNull(inputStream)) {
                return ok(inputStream);
            }
        } catch (Exception e) {
            Logger.debug(TAG, "  :  " + e.getMessage());
            e.printStackTrace();
        }
        return notFound("Content " + objectName + " not found in " + bucketName);
    }

}
