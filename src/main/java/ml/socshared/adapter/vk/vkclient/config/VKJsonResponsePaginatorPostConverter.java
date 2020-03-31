package ml.socshared.adapter.vk.vkclient.config;

import com.google.gson.*;
import ml.socshared.adapter.vk.vkclient.domain.Error;
import ml.socshared.adapter.vk.vkclient.domain.Paginator;
import ml.socshared.adapter.vk.vkclient.domain.Post;
import ml.socshared.adapter.vk.vkclient.domain.VKResponse;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class VKJsonResponsePaginatorPostConverter implements JsonSerializer<VKResponse<Paginator<Post>>>,
        JsonDeserializer<VKResponse<Paginator<Post>>> {
    @Override
    public VKResponse<Paginator<Post>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonElement error = obj.get("error");
        if(error != null) {
            return new VKResponse<>(context.deserialize(error, Error.class));
        } else {
            List<Post> posts = new LinkedList<>();


        }
    }

    @Override
    public JsonElement serialize(VKResponse<Paginator<Post>> src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
