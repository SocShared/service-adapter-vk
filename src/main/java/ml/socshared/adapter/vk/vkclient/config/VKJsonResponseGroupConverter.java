package ml.socshared.adapter.vk.vkclient.config;

import com.google.gson.*;
import ml.socshared.adapter.vk.vkclient.domain.Error;
import ml.socshared.adapter.vk.vkclient.domain.VKResponse;
import ml.socshared.adapter.vk.vkclient.domain.VkGroup;

import java.lang.reflect.Type;

public class VKJsonResponseGroupConverter implements JsonSerializer<VKResponse<VkGroup>>,
        JsonDeserializer<VKResponse<VkGroup>> {
    @Override
    public VKResponse<VkGroup> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonElement error = obj.get("error");
        if(error != null) {
            return new VKResponse<>(context.deserialize(error, Error.class));
        } else {
            return context.deserialize(obj.get("response"), VkGroup.class);
        }
    }

    @Override
    public JsonElement serialize(VKResponse<VkGroup> src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
