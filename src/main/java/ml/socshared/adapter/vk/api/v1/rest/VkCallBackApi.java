package ml.socshared.adapter.vk.api.v1.rest;

import java.util.HashMap;
import java.util.UUID;

public interface VkCallBackApi {
    String confirmService(UUID systemUserId, HashMap<String, String> json);
}
