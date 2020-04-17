package ml.socshared.adapter.vk.api.v1.rest;

import ml.socshared.adapter.vk.domain.response.CommentResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.SubCommentResponse;

import java.util.UUID;

public interface VkAdapterCommentApi {
    Page<CommentResponse> getCommentsOfPost(UUID systemUserId, String vkGroupId, String postId, int page, int count);
    CommentResponse getCommentOfPostById(UUID systemUserId, String vkGroupId, String postId, String commentId);
    SubCommentResponse getSubComment(UUID systemUserId, String vGroupId,String postId, String commentId, String subCommentId);
    Page<SubCommentResponse> getSubComments(UUID systemUserId, String vGroupId, String vkPostId, String commentId,
                                            int page, int size);
}
