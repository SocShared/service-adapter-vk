package ml.socshared.adapter.vk.api.v1.rest;

import io.swagger.annotations.Api;
import ml.socshared.adapter.vk.domain.request.PostRequest;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.PostResponse;

import java.util.Map;
import java.util.UUID;

@Api(value = "Api for working with vk posts through Adapter")
public interface VKAdapterPostAPI {
    PostResponse getPostOfGroupById(UUID userId, String groupId, String postId);
    Page<PostResponse> getPostsOfGroup(UUID userId, String groupId, int page, int size);
    public PostResponse addPostInGroup(UUID systemUserId, String groupId, PostRequest message);
    PostResponse updateAndGetPostInGroupById(UUID userId, String groupId, String postId, PostRequest message);
    void updatePostInGroupById(UUID userId, String groupId, String postId, PostRequest message);
    Map<String, String> removePostInGroupById(UUID userId, String groupId, String postId);
}
