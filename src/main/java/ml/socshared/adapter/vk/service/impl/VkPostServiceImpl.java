package ml.socshared.adapter.vk.service.impl;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.PostResponse;
import ml.socshared.adapter.vk.service.BaseFunctions;
import ml.socshared.adapter.vk.service.VkAuthorizationService;
import ml.socshared.adapter.vk.service.VkPostService;
import ml.socshared.adapter.vk.service.sentry.SentrySender;
import ml.socshared.adapter.vk.service.sentry.SentryTag;
import ml.socshared.adapter.vk.vkclient.VKClient;
import ml.socshared.adapter.vk.vkclient.domain.Paginator;
import ml.socshared.adapter.vk.vkclient.domain.Post;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class VkPostServiceImpl implements VkPostService {

    VkAuthorizationService vkAuth;
    VKClient client;
    SentrySender sentrySender;

    @Autowired
    VkPostServiceImpl(VkAuthorizationService vkAuth, VKClient client, SentrySender sentry) {
        this.vkAuth = vkAuth;
        this.client = client;
        this.sentrySender = sentry;
    }


    @Override
    public Page<PostResponse> getPostsOfGroup(UUID systemUserId, String vkGroupId, int page, int size) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);

        client.setToken(sUser.getAccessToken());
        int offset = page*size;
        Paginator<Post> posts = client.getPostsOfGroup(vkGroupId, "all", offset, size);
        List<PostResponse> response = new LinkedList<>();
        for(Post el : posts.getResponse()) {
            PostResponse resp = convertPostToPostResponseDefault(el);
            response.add(resp);

        }
        Page<PostResponse> result = new Page<>();
        result.setObject(response);
        result.setHasNext(posts.getCount() > (offset + size));
        result.setHasPrev(page > 0);
        result.setPage(page);
        result.setSize(size);

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_user_id", systemUserId);
        additional.put("group_id", vkGroupId);
        additional.put("page", page);
        additional.put("size", size);
        sentrySender.sentryMessage("get posts of vk group", additional,
                Collections.singletonList(SentryTag.GetPosts));

        return result;
    }

    @Override
    public PostResponse getPostById(UUID systemUserId, String vkGroupId, String vkPostId) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);

        Post vkPost = client.getPostOfGroup(vkGroupId, vkPostId);

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_user_id", systemUserId);
        additional.put("group_id", vkGroupId);
        additional.put("post_id", vkPostId);
        sentrySender.sentryMessage("get one post of vk group", additional,
                Collections.singletonList(SentryTag.GetPost));

        return convertPostToPostResponseDefault(vkPost);
    }

    @Override
    public PostResponse addPostToGroup(UUID systemUserId, String vkGroupId, String message) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);
        client.setToken(sUser.getAccessToken());


        String postId =  client.sendPostToGroup(vkGroupId, message);
        PostResponse result = new PostResponse();
        result.setPostId(postId);
        result.setMessage(message);
        result.setGroupId(vkGroupId);
        result.setSystemUserId(systemUserId);

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_user_id", systemUserId);
        additional.put("group_id", vkGroupId);
        additional.put("message", message);
        sentrySender.sentryMessage("add post to group", additional,
                Collections.singletonList(SentryTag.AddPost));

        return result;
    }

    @Override
    public void updatePostOfGroup(UUID systemUserId, String vkGroupId, String vkPostId, String message) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);
        client.setToken(sUser.getAccessToken());

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_user_id", systemUserId);
        additional.put("group_id", vkGroupId);
        additional.put("message", message);
        sentrySender.sentryMessage("update vk post", additional,
                Collections.singletonList(SentryTag.UpdatePost));

       client.editPostToGroup(vkGroupId, vkPostId, message);
    }

    @Override
    public void deletePostOfGroup(UUID systemUserId, String vkGroupId, String vkPostId) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);
        client.setToken(sUser.getVkUserId());
        client.setToken(sUser.getAccessToken());

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_user_id", systemUserId);
        additional.put("group_id", vkGroupId);
        additional.put("post_id", vkGroupId);
        sentrySender.sentryMessage("delete vk post", additional,
                Collections.singletonList(SentryTag.DeletePost));

        client.deletePost(vkGroupId, vkPostId);
    }

    public static PostResponse convertPostToPostResponseDefault(Post post) {
        final long ms = 1000;
        BaseFunctions.modifyOwnerId(post);
        PostResponse p = new PostResponse();
        p.setCreatedDate(new Date(post.getDate()*ms));
        p.setUpdateDate(new Date(0));
        p.setGroupId(String.valueOf(post.getOwnerId()));
        p.setLikesCount(post.getLikes().getCount());
        p.setRepostsCount(post.getReposts().getCount());
        p.setMessage(post.getText());
        p.setPostId(String.valueOf(post.getId()));
        p.setUserId(String.valueOf(post.getFromId()));
        p.setCommentsCount(post.getComments().getCount());
        if (post.getViews() != null)
            p.setViewsCount(post.getViews().getCount());
        return p;
    }

}
