package ml.socshared.adapter.vk.controller.v1;

import javassist.NotFoundException;
import ml.socshared.adapter.vk.api.v1.rest.VKAdapterPostAPI;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.PostResponse;
import ml.socshared.adapter.vk.service.ApplicationService;
import ml.socshared.adapter.vk.vkclient.VKClient;
import ml.socshared.adapter.vk.vkclient.domain.Paginator;
import ml.socshared.adapter.vk.vkclient.domain.Post;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


//TODO везде добавить проверку на существование access_token так, как он может быть пуст
@RestController
public class PostController implements VKAdapterPostAPI {
    @Override
    public PostResponse getPostOfGroupById(UUID userId, String groupId, String postId) throws NotFoundException {
        return null;
    }

    @Override
    @GetMapping("api/v1/users/{systemUserId}/groups/{groupId}/posts")
    public Page<PostResponse> getPostsOfGroup(@PathVariable("systemUserId") UUID userId,
                                              @PathVariable("groupId") String groupId,
                                              @RequestParam(value = "page", required = false, defaultValue ="1") int page,
                                              @RequestParam(value = "page", required = false, defaultValue ="10")int size)
            throws NotFoundException, VKClientException {
        logger.info("Request of get posts page");
        SystemUser sUser = appService.getUser(userId);
        VKClient client = new VKClient(sUser.getAccessToken());
        int offset = (page-1)*size;
        Paginator<Post> posts = client.getPostsOfGroup(groupId, "all", offset, size);
        List<PostResponse> response = new LinkedList<>();
        for(Post el : posts.getResponse()) {
            response.add(convertPostToPostResponseDefault(el));
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
    public PostResponse addPostInGroup(UUID userId, String groupId, String message) {
        return null;
    }

    @Override
    public PostResponse updateAndGetPostInGroupById(UUID userId, String groupId, String message) {
        return null;
    }

    @Override
    public void updatePostInGroupById(UUID userId, String groupId, String message) {

    }

    @Override
    public PostResponse removePostInGroupById(UUID userId, String groupId, String postId) {
        return null;
    }

    public static PostResponse convertPostToPostResponseDefault(Post post) {
        PostResponse p = new PostResponse();
        p.setCreatedDate(new Date(post.getDate()*1000));
        p.setUpdateDate(new Date(0));
        p.setGroupId(String.valueOf(post.getOwnerId()));
        p.setLikesCount(post.getLikes().getCount());
        p.setRepostsCount(post.getReposts().getCount());
        p.setMessage(post.getText());
        p.setViewsCount(post.getViews().getCount());
        return p;
    }

    private Logger logger = LoggerFactory.getLogger(PostController.class);
    @Autowired
    private ApplicationService appService;
}
