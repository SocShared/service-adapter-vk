package ml.socshared.adapter.vk.service;

import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.PostResponse;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;

import java.util.UUID;

public interface VkPostService {
    Page<PostResponse> getPostsOfGroup(UUID systemUserId, String vkGroupId, int page, int size) throws VKClientException;
    PostResponse getPostById(UUID systemUserId, String vkGroupId, String vkPostId) throws VKClientException;
    PostResponse addPostToGroup(UUID systemUserId, String vkGroupId, String message) throws VKClientException;
    void updatePostOfGroup(UUID systemUserId, String vkGroupId, String vkPostId, String message) throws VKClientException;
    void deletePostOfGroup(UUID systemUserId, String vkGroupId, String vkPostId) throws VKClientException;
}
