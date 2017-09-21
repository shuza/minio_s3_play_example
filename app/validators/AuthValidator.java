package validators;

import com.fasterxml.jackson.databind.JsonNode;
import configs.ParamsConfig;
import models.User;
import security.DataValidation;
import utils.CryptoUtil;

import java.util.ArrayList;

public class AuthValidator implements IValidator {
    private JsonNode params;
    private ArrayList<String> errors;
    private User user;

    public AuthValidator(JsonNode params) {
        this.params = params;
        this.errors = new ArrayList<String>();
        this.user = new User();
    }

    @Override
    public void validate() {
        String userEmail = DataValidation.getParams(params, ParamsConfig.PARAM_USER_EMAIL_KEY);
        if (DataValidation.isNull(userEmail)) {
            errors.add("Email address required.");
        } else if (!DataValidation.isEmail(userEmail)) {
            errors.add("Invalid email address.");
        }

        String userPassword = DataValidation.getParams(params, ParamsConfig.PARAM_USER_PASSWORD_KEY);
        if (DataValidation.isNull(userPassword)) {
            errors.add("Password required.");
        }
        user.setUserEmail(userEmail);
        user.setUserPassword(CryptoUtil.toPasswordHash(userPassword));
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
