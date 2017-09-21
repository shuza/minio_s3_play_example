package controllers;

import com.eclipsesource.json.JsonObject;
import com.fasterxml.jackson.databind.JsonNode;
import configs.ParamsConfig;
import daos.SessionDao;
import daos.UserDao;
import models.Session;
import models.User;
import play.libs.Json;
import play.mvc.Result;
import security.DataTransformation;
import security.DataValidation;
import utils.AppUtil;
import utils.CryptoUtil;
import validators.AuthValidator;

public class AuthController extends BaseController {
    private UserDao userDao = UserDao.getInstance();
    private SessionDao sessionDao = SessionDao.getInstance();

    public Result login() {
        try {
            JsonObject result = new JsonObject();
            JsonNode params = request().body().asJson();
            AuthValidator authValidator = new AuthValidator(params);
            authValidator.validate();
            if (authValidator.hasErrors()) {
                result.add("code", ParamsConfig.RESULT_ERROR);
                result.add("errors", DataTransformation.toJsonArray(authValidator.getErrors()));
            } else {
                User user = (User) authValidator.get();
                user = userDao.find(user.getUserEmail(), user.getUserPassword());
                if (DataValidation.isNull(user)) {
                    result.add("code", ParamsConfig.RESULT_FAILED);
                    result.add("response", ParamsConfig.PARAM_USER_EMAIL_KEY + " & "
                            + ParamsConfig.PARAM_USER_PASSWORD_KEY + " don't match.");
                } else {
                    Session session = CryptoUtil.getNewSession(user);
                    session.save();
                    result.add("code", ParamsConfig.RESULT_SUCCESS);
                    result.add("data", session.toJson());
                }
            }
            return ok(Json.parse(result.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            AppUtil.logError(this, e.getMessage());
        }
        return ok(Json.parse(getUnknownErrorResult().toString()));
    }

    public Result logout(String userHash, String accessToken) {
        try {
            JsonObject result = new JsonObject();
            User user = userDao.find(userHash);
            if (!DataValidation.isNull(user)) {
                Session session = sessionDao.find(user.getUserId(), accessToken);

                if (!DataValidation.isNull(session)) {
                    session.delete();
                    result.add("code", ParamsConfig.RESULT_SUCCESS);
                    result.add("response", "Session has been deleted");
                } else {
                    result.add("code", ParamsConfig.RESULT_FAILED);
                    result.add("response", "Invalid " + ParamsConfig.PARAM_ACCESS_TOKEN_KEY + ".");
                }
            } else {
                result.add("code", ParamsConfig.RESULT_FAILED);
                result.add("response", "Invalid " + ParamsConfig.PARAM_USER_HASH_KEY + ".");
            }
            return ok(Json.parse(result.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            AppUtil.logError(this, e.getMessage());
        }
        return ok(Json.parse(getUnknownErrorResult().toString()));
    }
}
