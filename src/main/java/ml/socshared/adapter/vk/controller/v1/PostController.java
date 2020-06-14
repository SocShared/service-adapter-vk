package ml.socshared.adapter.vk.controller.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.api.v1.rest.VKAdapterPostAPI;
import ml.socshared.adapter.vk.domain.request.PostRequest;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.domain.response.PostResponse;
import ml.socshared.adapter.vk.service.VkPostService;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


//TODO везде добавить проверку на существование access_token так, как он может быть пуст
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "api/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@RequiredArgsConstructor
public class PostController implements VKAdapterPostAPI {

    private final VkPostService postService;

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{systemUserId}/groups/{groupId}/posts/{postId}")
    public PostResponse getPostOfGroupById(@PathVariable("systemUserId") UUID userId,
                                           @PathVariable("groupId") String groupId,
                                           @PathVariable("postId") String postId) throws VKClientException {
        log.info("Request of get post of group by id");
        return postService.getPostById(userId, groupId, postId);
    }

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{systemUserId}/groups/{groupId}/posts")
    public Page<PostResponse> getPostsOfGroup(@PathVariable("systemUserId") UUID userId,
                                              @PathVariable("groupId") String groupId,
                                              @RequestParam(value = "page", required = false, defaultValue ="0") int page,
                                              @RequestParam(value = "size", required = false, defaultValue ="10")int size) throws VKClientException {
        log.info("Request of get posts page");
        return postService.getPostsOfGroup(userId, groupId, page, size);
    }

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping(value = "/private/users/{systemUserId}/groups/{groupId}/posts")
    public PostResponse addPostInGroup(@PathVariable UUID systemUserId,
                                       @PathVariable String groupId,
                                       @RequestBody PostRequest message) throws VKClientException {
        log.info("Request of create post");
        return postService.addPostToGroup(systemUserId, groupId, message.getMessage());
    }


    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @PatchMapping(value = "/private/users/{userId}/groups/{groupId}/posts/{postId}")
    public PostResponse updateAndGetPostInGroupById(@PathVariable UUID userId,
                                                    @PathVariable String groupId,
                                                    @PathVariable String postId,
                                                    @RequestBody PostRequest message) throws VKClientException {
        log.info("Patch request: Update post in group");
        postService.updatePostOfGroup(userId, groupId, postId, message.getMessage());
        return postService.getPostById(userId, groupId, postId);
    }

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @PutMapping(value = "/private/users/{userId}/groups/{groupId}/posts/{postId}")
    public void updatePostInGroupById(@PathVariable UUID userId,
                                      @PathVariable String groupId,
                                      @PathVariable String postId,
                                      @RequestBody PostRequest message) throws VKClientException {
        log.info("Put request: Update post in group");
        postService.updatePostOfGroup(userId, groupId, postId, message.getMessage());
    }

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping(value = "/private/users/{userId}/groups/{groupId}/posts/{postId}")
    public Map<String, String> removePostInGroupById(@PathVariable UUID userId,
                                                     @PathVariable String groupId,
                                                     @PathVariable String postId) throws VKClientException {
        log.info("Request delete of group post");
        postService.deletePostOfGroup(userId, groupId, postId);
        Map<String, String> response = new HashMap<>();
        response.put("systemUserId", String.valueOf(userId));
        response.put("groupId", groupId);
        response.put("postId", postId);
        return response;
    }
}
