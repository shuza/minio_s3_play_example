package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import configs.ParamsConfig;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import security.Authorizer;
import security.DataValidation;
import services.MinioBucketService;

@Security.Authenticated(Authorizer.class)
public class MinioBucketController extends BaseController {
    private String TAG = this.getClass().getSimpleName();

    private MinioBucketService minioBucketService = new MinioBucketService();

    public Result create() {
        try {
            JsonNode params = request().body().asJson();
            String bucketName = params.get(ParamsConfig.PARAM_BUCKET_KEY).asText();
            if (!DataValidation.isNull(bucketName)) {
                return ok(Json.parse(minioBucketService.create(bucketName).toString()));
            }
        } catch (Exception e) {
            Logger.debug(TAG, "  :  " + e.getMessage());
            e.printStackTrace();
        }
        return ok(Json.parse(getUnknownErrorResult().toString()));
    }

    public Result list() {
        return ok(minioBucketService.list().toString());
    }

    public Result remove(String bucketName) {
        try {
            return ok(Json.parse(minioBucketService.remove(bucketName).toString()));
        } catch (Exception e) {
            Logger.debug(TAG, "  :  " + e.getMessage());
            e.printStackTrace();
        }
        return ok(Json.parse(getUnknownErrorResult().toString()));
    }

    public Result check(String bucketName) {
        try {
            return ok(Json.parse(minioBucketService.check(bucketName).toString()));
        } catch (Exception e) {
            Logger.debug(TAG, "  :  " + e.getMessage());
            e.printStackTrace();
        }
        return ok(Json.parse(getUnknownErrorResult().toString()));
    }

}
