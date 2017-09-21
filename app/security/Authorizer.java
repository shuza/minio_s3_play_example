package security;

import com.eclipsesource.json.JsonObject;
import configs.ParamsConfig;
import daos.SessionDao;
import daos.UserDao;
import models.Session;
import models.User;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class Authorizer extends Security.Authenticator {
    private UserDao userDao = UserDao.getInstance();
    private SessionDao sessionDao = SessionDao.getInstance();

    @Override
    public String getUsername(Http.Context ctx) {
        Http.Request request = ctx.request();
        String userHash = request.getHeader(ParamsConfig.PARAM_USER_HASH_KEY);
        String accessToken = request.getHeader(ParamsConfig.PARAM_ACCESS_TOKEN_KEY);
        if (!DataValidation.isNull(userHash) && !DataValidation.isNull(accessToken)) {
            User user = userDao.find(userHash);
            if (!DataValidation.isNull(user)) {
                Session session = sessionDao.find(user.getUserId(), accessToken);
                return session != null ? session.getAccessToken() : null;
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        JsonObject result = new JsonObject();
        result.add("code", ParamsConfig.RESULT_UNAUTHORIZED);
        result.add("response", "Unauthorized user.");
        return ok(Json.parse(result.toString()));
    }
}
