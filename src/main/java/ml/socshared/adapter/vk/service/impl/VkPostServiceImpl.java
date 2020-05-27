package ml.socshared.adapter.vk.service.impl;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.PostResponse;
import ml.socshared.adapter.vk.service.BaseFunctions;
import ml.socshared.adapter.vk.service.VkAuthorizationService;
import ml.socshared.adapter.vk.service.VkPostService;
import ml.socshared.adapter.vk.vkclient.VKClient;
import ml.socshared.adapter.vk.vkclient.domain.Paginator;
import ml.socshared.adapter.vk.vkclient.domain.Post;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class VkPostServiceImpl implements VkPostService {

    VkAuthorizationService vkAuth;
    VKClient client;

    @Autowired
    VkPostServiceImpl(VkAuthorizationService vkAuth, VKClient client) {
        this.vkAuth = vkAuth;
        this.client = client;
    }


    @Override
    public Page<PostResponse> getPostsOfGroup(UUID systemUserId, String vkGroupId, int page, int size) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);

        BaseFunctions.checkGroupConnectionToUser(vkGroupId, sUser, log);

        client.setToken(sUser.getAccessToken());
        int offset = (page-1)*size;
        Paginator<Post> posts = client.getPostsOfGroup(vkGroupId, "all", offset, size);
        List<PostResponse> response = new LinkedList<>();
        for(Post el : posts.getResponse()) {
            PostResponse resp = convertPostToPostResponseDefault(el);
            response.add(resp);

        }
        Page<PostResponse> result = new Page<>();
        result.setObject(response);
        result.setHasNext(posts.getCount() > (offset + size));
        result.setHasPrev(page > 1);
        result.setPage(page);
        result.setSize(size);
        return result;
    }

    @Override
    public PostResponse getPostById(UUID systemUserId, String vkGroupId, String vkPostId) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);

        BaseFunctions.checkGroupConnectionToUser(vkGroupId, sUser, log);

        Post vkPost = client.getPostOfGroup(vkGroupId, vkPostId);
        return convertPostToPostResponseDefault(vkPost);
    }

    @Override
    public PostResponse addPostToGroup(UUID systemUserId, String vkGroupId, String message) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);
        client.setToken(sUser.getAccessToken());

        BaseFunctions.checkGroupConnectionToUser(vkGroupId, sUser, log);

        String postId =  client.sendPostToGroup(vkGroupId, message);
        PostResponse result = new PostResponse();
        result.setPostId(postId);
        result.setMessage(message);
        result.setGroupId(vkGroupId);
        result.setSystemUserId(systemUserId);
        return result;
    }

    @Override
    public void updatePostOfGroup(UUID systemUserId, String vkGroupId, String vkPostId, String message) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);
        client.setToken(sUser.getAccessToken());

        BaseFunctions.checkGroupConnectionToUser(vkGroupId, sUser, log);

       client.editPostToGroup(vkGroupId, vkPostId, message);
    }

    @Override
    public void deletePostOfGroup(UUID systemUserId, String vkGroupId, String vkPostId) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);
        client.setToken(sUser.getVkUserId());
        client.setToken(sUser.getAccessToken());

        BaseFunctions.checkGroupConnectionToUser(vkGroupId, sUser, log);

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
