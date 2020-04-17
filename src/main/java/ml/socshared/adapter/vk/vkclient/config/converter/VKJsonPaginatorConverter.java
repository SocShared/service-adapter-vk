package ml.socshared.adapter.vk.vkclient.config.converter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ml.socshared.adapter.vk.vkclient.domain.Paginator;
import ml.socshared.adapter.vk.vkclient.exception.JsonDeserializeException;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class VKJsonPaginatorConverter<T>  implements JsonSerializer<Paginator<T>>,
        JsonDeserializer<Paginator<T>> {

    private TypeToken<T> token;

    public VKJsonPaginatorConverter(TypeToken<T> t) {
        token = t;
    }

    @Override
    public Paginator<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject responseObject = json.getAsJsonObject();
        Paginator<T> pg = new Paginator<>();
        JsonElement count = responseObject.get("count");
        if(count == null) {
            throw new JsonDeserializeException("Invalid Json: Json don't containing element: response:count ");
        }
        pg.setCount(count.getAsInt());
        JsonElement itemsElement = responseObject.get("items");
        if(itemsElement == null) {
            throw new JsonDeserializeException("Invalid Json: Json don't containing element response:items");
        }
        JsonArray items = itemsElement.getAsJsonArray();
        List<T> elements = new LinkedList<>();
        for(JsonElement el : items) {
            elements.add(context.deserialize(el, token.getType()));
        }
            pg.setResponse(elements);
            return pg;
        }

    @Override
    public JsonElement serialize(Paginator<T> src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
