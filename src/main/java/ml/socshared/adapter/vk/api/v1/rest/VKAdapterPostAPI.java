package ml.socshared.adapter.vk.api.v1.rest;

import io.swagger.annotations.Api;
import javassist.NotFoundException;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.PostResponse;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;

import java.util.UUID;

@Api(value = "Api for working with vk posts through Adapter")
public interface VKAdapterPostAPI {
    PostResponse getPostOfGroupById(UUID userId, String groupId, String postId) throws NotFoundException;
    Page<PostResponse> getPostsOfGroup(UUID userId, String groupId, int page, int size) throws NotFoundException, VKClientException;
    PostResponse addPostInGroup(UUID userId, String groupId, String message);
    PostResponse updateAndGetPostInGroupById(UUID userId, String groupId, String message);
    void updatePostInGroupById(UUID userId, String groupId, String message);
    PostResponse removePostInGroupById(UUID userId, String groupId, String postId);
}
