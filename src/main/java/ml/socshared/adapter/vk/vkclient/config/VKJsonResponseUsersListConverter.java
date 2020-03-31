package ml.socshared.adapter.vk.vkclient.config;

import com.google.gson.*;
import ml.socshared.adapter.vk.vkclient.domain.Error;
import ml.socshared.adapter.vk.vkclient.domain.ErrorCode;
import ml.socshared.adapter.vk.vkclient.domain.User;
import ml.socshared.adapter.vk.vkclient.domain.VKResponse;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class VKJsonResponseUsersListConverter implements JsonSerializer<VKResponse<List<User>>>, JsonDeserializer<VKResponse<List<User>>> {
    @Override
    public JsonElement serialize(VKResponse<List<User>> src, Type typeOfSrc, JsonSerializationContext context) {
        if(src.isError()) {
            ErrorCode error = ErrorCode.valueOf("ddd");
            JsonObject json_error = new JsonObject();
            json_error.add("!error", context.serialize(src.getError()));
            json_error.add("=(", context.serialize("XD"));
            return json_error;

        } else {
            JsonArray ok = new JsonArray();
            List<User> users = src.getResponse();
            for(User el : users) {
                ok.add(context.serialize(el));
            }
            JsonObject ok_json = new JsonObject();
            ok_json.add("response", ok);
            return ok_json;
        }
    }


    @Override
    public VKResponse<List<User>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jobj = json.getAsJsonObject();
        JsonElement error = jobj.get("error");
        if (error == null) {
            //Ошибка отсутствует. Пришел результат
            JsonElement okResponse = jobj.get("response");
            JsonArray uarray =  okResponse.getAsJsonArray();
            List<User> users = new LinkedList<>();
            for(JsonElement el : uarray) {
                users.add(context.deserialize(el, User.class));
            }
            return new VKResponse<>(users);
        } else {
            //Возникла ошибка. Json является описанием ошибки
            Error user_error = context.deserialize(error, Error.class);
            return new VKResponse<>(user_error);
        }
    }


}
