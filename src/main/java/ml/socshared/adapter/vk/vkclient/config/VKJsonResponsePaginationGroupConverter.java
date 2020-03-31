package ml.socshared.adapter.vk.vkclient.config;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ml.socshared.adapter.vk.vkclient.domain.*;
import ml.socshared.adapter.vk.vkclient.exception.JsonDeserializeException;
import ml.socshared.adapter.vk.vkclient.domain.Error;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class VKJsonResponsePaginationGroupConverter implements JsonSerializer<VKResponse<Paginator<VkGroup>>>,
        JsonDeserializer<VKResponse<Paginator<VkGroup>>>{

    @Override
    public VKResponse<Paginator<VkGroup>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if(jsonObject == null) {
            throw new JsonDeserializeException("Invalid Json: Expecting JsonObject." +
                    "(Deserialize VKResponse<Paginator<List<Group>>>)");
        }
        JsonElement errorElement = jsonObject.get("error");
        if(errorElement != null) {
            //VK returned error
            JsonObject errorObject = errorElement.getAsJsonObject();
            Error error = context.deserialize(errorObject, Error.class);
            return new VKResponse<>(error);

        } else {
            //successful
            JsonElement responseElement =  jsonObject.get("response");
            if(responseElement == null) {
                throw new JsonDeserializeException("Invalid Json: Expecting response element of json. " +
                        "(Deserialize VKResponse<Paginator<List<Group>>>)");
            }
            JsonObject responseObject = responseElement.getAsJsonObject();
            Paginator<VkGroup> pg = new Paginator<>();
            JsonElement count = responseObject.get("count");
            if(count == null) {
                throw new JsonDeserializeException("Invalid Json: Json don't containing element: response:count ");
            }
            pg.setCount(count.getAsInt());
            JsonElement itemsElement = responseObject.get("items");
            if(itemsElement == null) {
                throw new JsonDeserializeException("Invalid Json: Json don't containing element^ response:iems");
            }
            JsonArray items = itemsElement.getAsJsonArray();
            List<VkGroup> groups = new LinkedList<>();
            for(JsonElement el : items) {
                groups.add(context.deserialize(el, VkGroup.class));
            }
            pg.setResponse(groups);
            return new VKResponse<>(pg);
        }
    }

    @Override
    public JsonElement serialize(VKResponse<Paginator<VkGroup>> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src.isError()) {
            JsonObject error = new JsonObject();
            error.add("error", context.serialize(src.getError(), Error.class));
            return error;
        } else {
            Paginator<VkGroup> data = src.getResponse();
            JsonObject ok = new JsonObject();
            JsonObject response = new JsonObject();
            response.add("count", context.serialize(data.getCount(), Integer.class));
            Type lgt = new TypeToken<List<VkGroup>>(){}.getType();
            response.add("items", context.serialize(data.getResponse(), lgt));
            ok.add("response", response);
            return ok;
        }
    }
}
