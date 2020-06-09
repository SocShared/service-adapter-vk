package ml.socshared.adapter.vk.api.v1.rest;

import ml.socshared.adapter.vk.domain.response.CommentResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.SubCommentResponse;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;

import java.util.UUID;

public interface VkAdapterCommentApi {
    Page<CommentResponse> getCommentsOfPost(UUID systemUserId, String vkGroupId, String postId, int page, int count) throws VKClientException;
    CommentResponse getCommentOfPostById(UUID systemUserId, String vkGroupId, String postId, String commentId) throws VKClientException;
    SubCommentResponse getSubComment(UUID systemUserId, String vGroupId,String postId, String commentId, String subCommentId) throws VKClientException;
    Page<SubCommentResponse> getSubComments(UUID systemUserId, String vGroupId, String vkPostId, String commentId,
                                            int page, int size) throws VKClientException;
}
