package ml.socshared.adapter.vk.service;

import ml.socshared.adapter.vk.domain.response.GroupResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;

import java.util.UUID;

public interface VkGroupService {
    Page<GroupResponse> getUserGroups(UUID systemUserId, int page, int size) throws VKClientException;
    GroupResponse getUserGroupbyId(UUID systemUserId, String vkGroupId) throws VKClientException;
   }
