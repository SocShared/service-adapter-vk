package ml.socshared.adapter.vk.vkclient.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class VkGroup {
    private String id;
    private String name;
    private String description;
   // private String photo;
    private String deactivated;
    private GroupType type;
    private Map<String, String> additionalFields = new HashMap<>();//TODO дописать сериализатор для доп полей

    public boolean isDeactivated() {
        return deactivated == null;
    }

}
