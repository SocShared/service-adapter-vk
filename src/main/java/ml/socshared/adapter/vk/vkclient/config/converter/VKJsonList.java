package ml.socshared.adapter.vk.vkclient.config.converter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class VKJsonList<T>  implements JsonSerializer<List<T>>,
        JsonDeserializer<List<T>> {

    TypeToken<T> token;
    public VKJsonList(TypeToken<T> t) {
        token = t;
    }
    @Override
    public List<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jarray = json.getAsJsonArray();
        List<T> res = new LinkedList<>();
        for(JsonElement el : jarray) {
            res.add(context.deserialize(el, token.getType()));
        }
        return res;
    }

    @Override
    public JsonElement serialize(List<T> src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
