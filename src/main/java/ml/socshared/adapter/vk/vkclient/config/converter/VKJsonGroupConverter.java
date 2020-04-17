package ml.socshared.adapter.vk.vkclient.config.converter;

import com.google.gson.*;
import ml.socshared.adapter.vk.vkclient.domain.GroupType;
import ml.socshared.adapter.vk.vkclient.domain.VkGroup;
import ml.socshared.adapter.vk.vkclient.exception.JsonDeserializeException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class VKJsonGroupConverter  implements JsonSerializer<VkGroup>,
        JsonDeserializer<VkGroup> {
    @Override
    public VkGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
       Map<String, JsonTransformationToGroup> parsers = new HashMap<>();
       parsers.put("id", new TransformID());
       parsers.put("name", new TransformName());
       parsers.put("type", new TransformType());
       parsers.put("deactivated", new TransformDeactivated());
        VkGroup vkGroup = new VkGroup();
        for(Map.Entry<String, JsonElement> el : obj.entrySet()) {
            if(parsers.containsKey(el.getKey())) {
                parsers.get(el.getKey()).transform(el.getValue(), vkGroup);
            } else {
                vkGroup.getAdditionalFields().put(el.getKey(), el.getValue().getAsString());
            }
        }

        if(vkGroup.getId() == null) {
            throw new JsonDeserializeException("Json of Group invalid: Json don't containing id field");
        }
        if(vkGroup.getName() == null) {
            throw new JsonDeserializeException("Json of Group invalid: Json don't containing name field");
        }
        if(vkGroup.getType() == null) {
            throw new JsonDeserializeException("Json of Group invalid: Json don't containing type field");
        }
        return vkGroup;

    }
    @Override
    public JsonElement serialize(VkGroup src, Type typeOfSrc, JsonSerializationContext context) {
        //TODO group serializer
        return null;
    }
}

interface JsonTransformationToGroup {
    public void transform(JsonElement json, VkGroup g);
}

class TransformID implements  JsonTransformationToGroup {
    @Override
    public void transform(JsonElement json, VkGroup g) {
        g.setId(json.getAsString());
    }
}

class TransformName implements  JsonTransformationToGroup {
    @Override
    public void transform(JsonElement json, VkGroup g) {
        g.setName(json.getAsString());
    }
}

class TransformType implements  JsonTransformationToGroup {
    @Override
    public void transform(JsonElement json, VkGroup g) {
        g.setType(GroupType.fromString(json.getAsString()));
    }
}
class TransformDeactivated implements JsonTransformationToGroup {

    @Override
    public void transform(JsonElement json, VkGroup g) {
        g.setDeactivated(json.getAsString());
    }
}