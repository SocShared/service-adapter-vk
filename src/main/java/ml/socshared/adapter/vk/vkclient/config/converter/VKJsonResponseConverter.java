package ml.socshared.adapter.vk.vkclient.config.converter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ml.socshared.adapter.vk.vkclient.domain.ErrorType;
import ml.socshared.adapter.vk.vkclient.domain.VKResponse;

import java.lang.reflect.Type;

public class VKJsonResponseConverter<T>  implements JsonSerializer<VKResponse<T>>,
        JsonDeserializer<VKResponse<T>> {

    private TypeToken<T> token;

    public VKJsonResponseConverter(TypeToken<T> t) {
        token = t;
    }

    @Override
    public VKResponse<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonElement error = obj.get("error");
        if(error != null) {
            return new VKResponse<>(context.deserialize(error, ErrorType.class));
        } else {
            JsonElement response = obj.get("response");
            VKResponse<T> ok = new VKResponse<>();
            ok.setResponse(context.deserialize(response, token.getType()));
            return ok;
        }
    }

    @Override
    public JsonElement serialize(VKResponse<T> src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
