package security;

import com.eclipsesource.json.JsonArray;

import java.util.List;

public class DataTransformation {
    public static JsonArray toJsonArray(List<String> list) {
        JsonArray values = new JsonArray();
        for (String item : list) {
            values.add(item);
        }
        return values;
    }
}
