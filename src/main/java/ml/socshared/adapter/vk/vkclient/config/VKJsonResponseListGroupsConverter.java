package ml.socshared.adapter.vk.vkclient.config;

import com.google.gson.*;
import ml.socshared.adapter.vk.vkclient.domain.Error;
import ml.socshared.adapter.vk.vkclient.domain.VKResponse;
import ml.socshared.adapter.vk.vkclient.domain.VkGroup;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class VKJsonResponseListGroupsConverter implements JsonSerializer<VKResponse<List<VkGroup>>>,
        JsonDeserializer<VKResponse<List<VkGroup>>> {

    @Override
    public VKResponse<List<VkGroup>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonElement error = obj.get("error");
        if (error != null) {
            return new VKResponse<>(context.deserialize(error, Error.class));
        } else {
            JsonArray response = obj.get("response").getAsJsonArray();
            List<VkGroup> groups = new LinkedList<>();
            for (JsonElement el : response) {
                groups.add(context.deserialize(el, VkGroup.class));
            }
            return new VKResponse<>(groups);
        }
    }

    @Override
    public JsonElement serialize(VKResponse<List<VkGroup>> src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
