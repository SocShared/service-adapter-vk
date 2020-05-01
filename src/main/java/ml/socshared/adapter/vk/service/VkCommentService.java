package ml.socshared.adapter.vk.service;

import ml.socshared.adapter.vk.domain.response.CommentResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.SubCommentResponse;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;

import java.util.UUID;

public interface VkCommentService {
    CommentResponse getCommentOfPost(UUID systemUserId, String vkGroupId, String vkPostId, String vkCommentId)
            throws VKClientException;
    Page<CommentResponse> getCommentsOfPost(UUID systemUserId, String vkGroupId, String vkPostId, int page, int size)
            throws VKClientException;
    Page<SubCommentResponse> getSubComments(UUID systemUserId, String vkGroupId, String vkPostId, String vkSuperCommentId,
                                            int page, int size) throws VKClientException;
    SubCommentResponse getSubCommentById(UUID systemUserId, String vkGroupId, String vkPostId, String vkSuperCommentId,
                                         String vkSubCommentId) throws VKClientException;
}
