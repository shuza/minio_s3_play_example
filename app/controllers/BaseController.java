package controllers;

import com.eclipsesource.json.JsonObject;
import configs.ParamsConfig;
import play.mvc.Controller;

public class BaseController extends Controller {

    public JsonObject getUnknownErrorResult() {
        return new JsonObject().add("code", ParamsConfig.RESULT_ERROR)
                .add("response", "Something went wrong!!");
    }

    public JsonObject getUnknownErrorResult(String message) {
        return new JsonObject().add("code", ParamsConfig.RESULT_ERROR)
                .add("response", "Something went wrong!!")
                .add("message", message);
    }

}
