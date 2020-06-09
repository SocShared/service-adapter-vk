package ml.socshared.adapter.vk.vkclient.config.converter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import ml.socshared.adapter.vk.vkclient.domain.VkOnline;

import java.lang.reflect.Type;

public class VKJsonOnlineConverter implements JsonDeserializer<VkOnline> {
    @Override
    public VkOnline deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Integer count = json.getAsJsonObject().get("count").getAsInt();
        return new VkOnline(count);
    }
}
