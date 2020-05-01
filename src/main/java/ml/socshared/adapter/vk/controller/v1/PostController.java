package ml.socshared.adapter.vk.controller.v1;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.api.v1.rest.VKAdapterPostAPI;
import ml.socshared.adapter.vk.domain.request.PostRequest;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.PostResponse;
import ml.socshared.adapter.vk.exception.impl.HttpInternalServerErrorException;
import ml.socshared.adapter.vk.service.VkPostService;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


//TODO везде добавить проверку на существование access_token так, как он может быть пуст
@RestController
@RequestMapping("api/v1")
@Slf4j
public class PostController implements VKAdapterPostAPI {

    private VkPostService postService;

    @Autowired
    PostController(VkPostService ps) {
        postService = ps;
    }

    @Override
    @GetMapping("/users/{systemUserId}/groups/{groupId}/posts/{postId}")
    public PostResponse getPostOfGroupById(@PathVariable("systemUserId") UUID userId,
                                           @PathVariable("groupId") String groupId,
                                           @PathVariable("postId") String postId) {
        log.info("Request of get post of group by id");
        try {
            return postService.getPostById(userId, groupId, postId);
        } catch (VKClientException e) {
            String msg = "VkClient error: " + e.getMessage();
            log.warn(msg);
            throw new HttpInternalServerErrorException(msg);
        }
    }

    @Override
    @GetMapping("/users/{systemUserId}/groups/{groupId}/posts")
    public Page<PostResponse> getPostsOfGroup(@PathVariable("systemUserId") UUID userId,
                                              @PathVariable("groupId") String groupId,
                                              @RequestParam(value = "page", required = false, defaultValue ="1") int page,
                                              @RequestParam(value = "size", required = false, defaultValue ="10")int size) {
        log.info("Request of get posts page");
        try {
            return postService.getPostsOfGroup(userId, groupId, page, size);
        } catch (VKClientException e) {
            String msg = "VkClient Error: " + e.getMessage();
            log.warn(msg);
            throw new HttpInternalServerErrorException(msg);
        }
    }

    @Override
    @PostMapping(value = "/users/{systemUserId}/groups/{groupId}/posts",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public PostResponse addPostInGroup(@PathVariable UUID systemUserId,
                                       @PathVariable String groupId,
                                       @RequestBody PostRequest message) {
        log.info("Request of create post");
        try {
            return postService.addPostToGroup(systemUserId, groupId, message.getMessage());
        } catch (VKClientException e) {
            String msg = "VkClient Error: " + e.getMessage();
            log.warn(msg);
            throw new HttpInternalServerErrorException(msg);
        }
    }


    @Override
    @PatchMapping(value = "/users/{userId}/groups/{groupId}/posts/{postId}",
        consumes = {MediaType.APPLICATION_JSON_VALUE})
    public PostResponse updateAndGetPostInGroupById(@PathVariable UUID userId,
                                                    @PathVariable String groupId,
                                                    @PathVariable String postId,
                                                    @RequestBody PostRequest message) {
        log.info("Patch request: Update post in group");
        try {
            postService.updatePostOfGroup(userId, groupId, postId, message.getMessage());
            return postService.getPostById(userId, groupId, postId);
        } catch (VKClientException e) {
           String msg = "VkClient Error: " + e.getMessage() + "(Code: " + e.getErrorType() + ")";
           log.warn(msg);
           throw new HttpInternalServerErrorException(msg);
        }
    }

    @Override
    @PutMapping(value = "/users/{userId}/groups/{groupId}/posts/{postId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updatePostInGroupById(@PathVariable UUID userId,
                                      @PathVariable String groupId,
                                      @PathVariable String postId,
                                      @RequestBody PostRequest message) {
        log.info("Put request: Update post in group");
        try {
            postService.updatePostOfGroup(userId, groupId, postId, message.getMessage());
        } catch (VKClientException e) {
            String msg = "VkClient Error: " + e.getMessage() + "(Code: " + e.getErrorType() + ")";
            log.warn(msg);
            throw new HttpInternalServerErrorException(msg);
        }
    }

    @Override
    @DeleteMapping(value = "/users/{userId}/groups/{groupId}/posts/{postId}")
    public Map<String, String> removePostInGroupById(@PathVariable UUID userId,
                                                     @PathVariable String groupId,
                                                     @PathVariable String postId) {
        log.info("Request delete of group post");
        try {
         postService.deletePostOfGroup(userId, groupId, postId);
         Map<String, String> response = new HashMap<>();
         response.put("systemUserId", String.valueOf(userId));
         response.put("groupId", groupId);
         response.put("postId", postId);
         return response;
         
        } catch (VKClientException e) {
            String msg = "VkClien Error: " + e.getMessage() + "(Code: " + e.getErrorType() + ")";
            log.warn(msg);
            throw new HttpInternalServerErrorException(msg);
        }
    }
}
