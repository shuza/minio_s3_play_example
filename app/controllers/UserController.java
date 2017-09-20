package controllers;

import com.eclipsesource.json.JsonObject;
import com.fasterxml.jackson.databind.JsonNode;
import configs.ParamsConfig;
import daos.UserDao;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import security.DataTransformation;
import utils.AppUtil;
import validators.UserValidator;

public class UserController extends BaseController {
    private UserDao userDao = UserDao.getInstance();

    public Result create() {
        try {
            JsonObject result = new JsonObject();
            JsonNode params = request().body().asJson();
            UserValidator userValidator = new UserValidator(params);
            userValidator.validate();
            if (userValidator.hasErrors()) {
                result.add("errors", DataTransformation.toJsonArray(userValidator.getErrors()));
                result.add("code", ParamsConfig.RESULT_ERROR);
            } else {
                User user = (User) userValidator.get();
                if (!userDao.isEmailExists(user.getUserEmail())) {
                    user.save();
                    result.add("code", ParamsConfig.RESULT_SUCCESS);
                    result.add("message", "User has been created successfully");
                    result.add("data", user.toJson());
                } else {
                    result.add("code", ParamsConfig.RESULT_FAILED);
                    result.add("message", ParamsConfig.PARAM_USER_NAME_KEY + " already exist");
                }
            }
            return ok(Json.parse(result.toString()));
        } catch (Exception e) {
            AppUtil.logError(this, e.getMessage());
        }

        return ok(Json.parse(getUnknownErrorResult().toString()));
    }

}
