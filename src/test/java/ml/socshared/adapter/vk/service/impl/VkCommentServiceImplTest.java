package ml.socshared.adapter.vk.service.impl;

import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.domain.response.CommentResponse;
import ml.socshared.adapter.vk.service.VkAuthorizationService;
import ml.socshared.adapter.vk.vkclient.VKClient;
import ml.socshared.adapter.vk.vkclient.domain.VkComment;
import ml.socshared.adapter.vk.vkclient.domain.part.CommentThread;
import ml.socshared.adapter.vk.vkclient.domain.part.Likes;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.UUID;

class VkCommentServiceImplTest {

    VKClient client = Mockito.mock(VKClient.class);
    VkAuthorizationService auth = Mockito.mock(VkAuthorizationService.class);
    VkCommentServiceImpl service = new VkCommentServiceImpl(auth, client);




    @Test
    void getCommentsOfPost() {
    }

    @Test
    void getCommentOfPost() throws VKClientException {
        final String vkGroupId = "465465465";
        final UUID sUserId = UUID.randomUUID();
        final String accessToken = "sdfsdf5745sad4f654sdf";
        final int vkCommentId = 19;
        final int vkPostId = 1000;
        VkComment comment = new VkComment();
        comment.setCanEdit(1);
        comment.setDate(0);
        comment.setFromId(vkPostId);
        comment.setId(vkCommentId);
        Likes likes = new Likes();
        likes.setCanLike(1);
        likes.setCanPublish(1);
        likes.setCount(51);
        likes.setUserLikes(1);
        comment.setLikes(likes);
        comment.setText("Test comment text");
        CommentThread thread = new CommentThread();
        thread.setCount(5);
        comment.setThread(thread);
        comment.setPostId(vkPostId);

        Mockito.doReturn(comment)
                .when(client)
                .getCommentById(ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyInt(),
                        ArgumentMatchers.anyInt());

        SystemUser sUser = new SystemUser();
        sUser.setGroupVkId(vkGroupId);
        sUser.setAccessToken(accessToken);
        sUser.setId(sUserId);

        Mockito.doReturn(sUser)
                .when(auth)
                .getUser(ArgumentMatchers.eq(sUserId));
        CommentResponse result = service.getCommentOfPost(sUserId, vkGroupId, String.valueOf(vkPostId), String.valueOf(vkCommentId), 0, 1);

        Assertions.assertEquals(Integer.parseInt(result.getCommentId()), comment.getId());
    }

    @Test
    void getCommentsOfSuperComment() {
    }

    @Test
    void getCommentOfSuperComment() {
    }
}