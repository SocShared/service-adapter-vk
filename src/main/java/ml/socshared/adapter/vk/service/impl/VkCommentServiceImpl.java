package ml.socshared.adapter.vk.service.impl;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.domain.response.CommentResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.SubCommentResponse;
import ml.socshared.adapter.vk.exception.impl.HttpBadRequestException;
import ml.socshared.adapter.vk.exception.impl.HttpNotFoundException;
import ml.socshared.adapter.vk.service.VkAuthorizationService;
import ml.socshared.adapter.vk.service.VkCommentService;
import ml.socshared.adapter.vk.service.sentry.SentrySender;
import ml.socshared.adapter.vk.service.sentry.SentryTag;
import ml.socshared.adapter.vk.vkclient.VKClient;
import ml.socshared.adapter.vk.vkclient.domain.Paginator;
import ml.socshared.adapter.vk.vkclient.domain.VkComment;
import ml.socshared.adapter.vk.vkclient.domain.VkSubComment;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class VkCommentServiceImpl implements VkCommentService {

    private VkAuthorizationService vkAuth;
    private VKClient client;
    private SentrySender sentrySender;

    @Autowired
    VkCommentServiceImpl(VkAuthorizationService auth, VKClient vkClient, SentrySender sentry) {
        vkAuth = auth;
        client = vkClient;
        sentrySender = sentry;
    }


    @Override
    public Page<CommentResponse> getCommentsOfPost(UUID systemUserId, String vkGroupId, String vkPostId,
                                                   int page, int size) throws VKClientException {
        getUser(systemUserId);
        int offset = page*size;
        Paginator<VkComment> comments = client.getCommentsOfPost(vkGroupId, vkPostId, offset, size);
        List<CommentResponse> comm = new LinkedList<>();
        for(VkComment comment : comments.getResponse()) {
            CommentResponse tmp = convertVkCommentToCommentResponseDefault(comment);
            tmp.setGroupId(vkGroupId);
            tmp.setSystemUserId(systemUserId);
            comm.add(tmp);
        }
        Page<CommentResponse> response = new Page<>();
        response.setObject(comm);
        response.setPage(page);
        response.setHasPrev(page > 0);
        response.setHasNext(comments.getCount() > page*comm.size());

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_user_id", systemUserId);
        additional.put("group_id", vkGroupId);
        additional.put("post_id", vkPostId);
        additional.put("page", page);
        additional.put("size", size);
        sentrySender.sentryMessage("get comments of vk post",
                additional, Collections.singletonList(SentryTag.CommentsOfPost));


        return response;
    }


    @Override
    public CommentResponse getCommentOfPost(UUID systemUserId, String vkGroupId, String vkPostId, String vkCommentId) throws VKClientException {
        getUser(systemUserId);
        VkComment comment = client.getCommentById(vkGroupId, vkCommentId);
        if(!vkPostId.equals(String.valueOf(comment.getPostId()))) {
            throw new HttpBadRequestException("Post (" + vkPostId + ") don't have a comment with id " + vkCommentId);
        }
        CommentResponse response = convertVkCommentToCommentResponseDefault(comment);
        response.setGroupId(vkGroupId);
        response.setSystemUserId(systemUserId);

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_user_id", systemUserId);
        additional.put("group_id", vkGroupId);
        additional.put("post_id", vkPostId);
        sentrySender.sentryMessage("get one comment of vk post",
                additional, Collections.singletonList(SentryTag.CommentOfPost));


        return response;
    }

    @Override
    public Page<SubCommentResponse> getSubComments(UUID systemUserId, String vkGroupId, String vkPostId,
                                                   String vkSuperCommentId, int page, int size) throws VKClientException {
        SystemUser sUser = getUser(systemUserId);
        int offset = page*size;
        Paginator<VkSubComment> comments = client.getSubComments(vkGroupId, vkPostId, vkSuperCommentId, offset, size);
        List<SubCommentResponse> response = new LinkedList<>();
        for(VkSubComment c : comments.getResponse()) {
            SubCommentResponse scr = new SubCommentResponse(convertVkCommentToCommentResponseDefault(c));
            scr.setSuperCommentId(vkSuperCommentId);
            scr.setSystemUserId(sUser.getId());
            scr.setGroupId(vkGroupId);
            response.add(scr);
        }
        Page<SubCommentResponse> responsePage = new Page<>();
        responsePage.setObject(response);
        responsePage.setPage(page);
        responsePage.setHasPrev(page > 1);
        responsePage.setHasNext(page*response.size() < comments.getCount());
        responsePage.setSize(response.size());

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_user_id", systemUserId);
        additional.put("group_id", vkGroupId);
        additional.put("post_id", vkPostId);
        additional.put("comment_id", vkSuperCommentId);
        sentrySender.sentryMessage("get one comment of vk post",
                additional, Collections.singletonList(SentryTag.GetSubComments));

        return responsePage;

    }

    @Override
    public SubCommentResponse getSubCommentById(UUID systemUserId, String vkGroupId, String vkPostId,
                                                String vkSuperCommentId, String vkSubCommentId) throws VKClientException {
        SystemUser sUser = getUser(systemUserId);
        int sId = Integer.parseInt(vkSubCommentId);
        int residual = 1;
        int size = 10;
        int i = 0;
        boolean isFound = false;
        VkSubComment foundedComment = null;
        while(residual > 0) {
            int offset = i*size;
            Paginator<VkSubComment> comments = client.getSubComments(vkGroupId, vkPostId, vkSuperCommentId, offset, size);
            int count = comments.getCount();
            residual = count - (offset + size);
            for(VkSubComment el : comments.getResponse()) {
                if(sId == el.getId()) {
                    foundedComment = el;
                    isFound = true;
                    break;
                }
            }
            if(isFound) {
                break;
            }
            i++;
        }

        if(isFound) {
            SubCommentResponse response = new SubCommentResponse(
                    convertVkCommentToCommentResponseDefault(foundedComment));
            response.setSuperCommentId(vkSuperCommentId);
            response.setGroupId(vkGroupId);
            response.setSystemUserId(sUser.getId());


            Map<String, Object> additional = new HashMap<>();
            additional.put("system_user_id", systemUserId);
            additional.put("group_id", vkGroupId);
            additional.put("post_id", vkPostId);
            additional.put("comment_id", vkSuperCommentId);
            additional.put("sub_comment_id", vkSubCommentId);
            sentrySender.sentryMessage("get one comment of vk post",
                    additional, Collections.singletonList(SentryTag.GetSubCommentById));

            return response;
        }
        throw new HttpNotFoundException("Comment (id: " + vkSuperCommentId + ") do't contain Sub Comment (id: "+
                                        vkSubCommentId);
    }

    public CommentResponse convertVkCommentToCommentResponseDefault(VkComment comment) {
        final long ms = 1000;
        CommentResponse cr = new CommentResponse();
        cr.setCommentId(String.valueOf(comment.getId()));
        cr.setCreatedDate(new Date(comment.getDate()*ms));
        cr.setPostId(String.valueOf(comment.getPostId()));
        cr.setMessage(comment.getText());
        cr.setUpdateDate(new Date(0));
        cr.setLikesCount(comment.getLikes().getCount());
        if(comment.getThread() != null) {
            cr.setSubCommentCount(comment.getThread().getCount());
        }

        return cr;
    }

    public SystemUser getUser(UUID user) {
        SystemUser sUser = vkAuth.getUser(user);
        client.setToken(sUser.getAccessToken());
        return sUser;
    }
}
