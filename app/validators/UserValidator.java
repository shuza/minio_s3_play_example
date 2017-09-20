package validators;

import com.eclipsesource.json.JsonObject;
import com.fasterxml.jackson.databind.JsonNode;
import configs.ParamsConfig;
import models.User;
import security.DataValidation;
import utils.AppUtil;
import utils.CryptoUtil;

import java.util.ArrayList;

public class UserValidator implements IValidator {
    private JsonNode params;
    private ArrayList<String> errors;
    private User user;

    public UserValidator(JsonNode params) {
        this.params = params;
        errors = new ArrayList<String>();
        user = new User();
    }

    @Override
    public void validate() {
        user.setUserName(DataValidation.getParams(params, ParamsConfig.PARAM_USER_NAME_KEY));
        if (DataValidation.isNull(user.getUserName())) {
            errors.add(ParamsConfig.PARAM_USER_NAME_KEY + " is required.");
        }

        user.setUserEmail(DataValidation.getParams(params, ParamsConfig.PARAM_USER_EMAIL_KEY));
        if (DataValidation.isNull(user.getUserEmail())) {
            errors.add(ParamsConfig.PARAM_USER_EMAIL_KEY + " is required.");
        } else if (!DataValidation.isEmail(user.getUserEmail())) {
            errors.add("Invalid email address.");
        }

        user.setUserPassword(DataValidation.getParams(params, ParamsConfig.PARAM_USER_PASSWORD_KEY));
        if (DataValidation.isNull(user.getUserPassword())) {
            errors.add(ParamsConfig.PARAM_USER_PASSWORD_KEY + " is required.");
        } else {
            user.setUserPassword(CryptoUtil.toPasswordHash(user.getUserPassword()));
        }

        user.setUserHash(AppUtil.getUUID());
        user.setUserId(AppUtil.getUUID());
    }

    @Override
    public Object get() {
        return user;
    }

    @Override
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public ArrayList<String> getErrors() {
        return errors;
    }
}
