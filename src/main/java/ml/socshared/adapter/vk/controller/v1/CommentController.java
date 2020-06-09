package ml.socshared.adapter.vk.controller.v1;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.api.v1.rest.VkAdapterCommentApi;
import ml.socshared.adapter.vk.domain.response.CommentResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.SubCommentResponse;
import ml.socshared.adapter.vk.service.VkCommentService;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("api/v1")
@Slf4j
public class CommentController implements VkAdapterCommentApi {
    private VkCommentService service;

    @Autowired
    public CommentController(VkCommentService service) {
        this.service = service;
    }


    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{systemUserId}/groups/{vkGroupId}/posts/{postId}/comments")
    @Override
    public  Page<CommentResponse> getCommentsOfPost(@PathVariable UUID systemUserId, @PathVariable String vkGroupId,
                                                    @PathVariable String postId,
                                                    @RequestParam(name="page", required=false, defaultValue="0") int page,
                                                    @RequestParam(name="count", required=false, defaultValue="10") int count) throws VKClientException {
        log.info("Request of get post's comments");
        return service.getCommentsOfPost(systemUserId, vkGroupId, postId, page, count);
    }



    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{systemUserId}/groups/{vkGroupId}/posts/{postId}/comments/{commentId}")
    @Override
    public CommentResponse getCommentOfPostById(@PathVariable UUID systemUserId,@PathVariable String vkGroupId,
                                                @PathVariable String postId,
                                                @PathVariable String commentId) throws VKClientException {
        log.info("Request of get post's comment");
        return service.getCommentOfPost(systemUserId, vkGroupId, postId, commentId);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{systemUserId}/groups/{vkGroupId}/posts/{postId}/comments/{commentId}/sub_comments")
    @Override
    public Page<SubCommentResponse> getSubComments(@PathVariable UUID systemUserId,@PathVariable String vkGroupId,
                                                   @PathVariable String postId, @PathVariable String commentId,
                                                   @RequestParam(name="page", required=false, defaultValue="0") int page,
                                                   @RequestParam(name="size", required=false, defaultValue= "10") int size) throws VKClientException {
        log.info("Request of get subcomments");
        return service.getSubComments(systemUserId, vkGroupId, postId, commentId, page, size);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{systemUserId}/groups/{vkGroupId}/posts/{postId}/comments/{commentId}/sub_comments/{subCommentId}")
    @Override
    public SubCommentResponse getSubComment(@PathVariable UUID systemUserId,@PathVariable String vkGroupId,
                                            @PathVariable String postId, @PathVariable String commentId,
                                            @PathVariable String subCommentId) throws VKClientException {
        log.info("Request of get subcomment by id");
        return service.getSubCommentById(systemUserId, vkGroupId, postId, commentId, subCommentId);

    }
}

