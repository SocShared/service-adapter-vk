package ml.socshared.adapter.vk.vkclient.config.converter;

import com.google.gson.*;
import ml.socshared.adapter.vk.vkclient.domain.VkComment;
import ml.socshared.adapter.vk.vkclient.exception.JsonDeserializeException;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class VKJsonOneCommentConverter  implements JsonSerializer<List<VkComment>>,
        JsonDeserializer<List<VkComment>> {

    @Override
    public List<VkComment> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject commentObject = json.getAsJsonObject();
        JsonElement el = commentObject.get("items");
        if(el == null) {
            throw new JsonDeserializeException("invalid json of one comment: " + commentObject.toString());
        }
        JsonArray jarray = el.getAsJsonArray();
        return Collections.singletonList(context.deserialize(jarray.get(0), VkComment.class));
    }

    @Override
    public JsonElement serialize(List<VkComment> src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
