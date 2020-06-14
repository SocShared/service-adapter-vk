package ml.socshared.adapter.vk.controller.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.api.v1.rest.VkAdapterGroupApi;
import ml.socshared.adapter.vk.domain.response.GroupResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.exception.impl.HttpNotFoundException;
import ml.socshared.adapter.vk.service.VkGroupService;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "api/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@RequiredArgsConstructor
public class GroupController implements VkAdapterGroupApi {

    private final VkGroupService groupService;

    @PreAuthorize("hasRole('SERVICE')")
   // @Override
    @GetMapping("/private/users/{systemUserId}/groups")
    public Page<GroupResponse> getGroupsList
            (@PathVariable("systemUserId")                                 UUID userId,
             @RequestParam(name="page", required=false, defaultValue="0")  int page,
             @RequestParam(name="size", required=false, defaultValue="10") int size)
            throws HttpNotFoundException, VKClientException {
            log.info("Request get of Groups list");
            return groupService.getUserGroups(userId, page, size);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{systemUserId}/groups/{groupId}")
    GroupResponse getGroupById(@PathVariable UUID systemUserId, @PathVariable String groupId) throws VKClientException {
        log.info("Request of get group info by id");
        return groupService.getUserGroupbyId(systemUserId, groupId);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{systemUserId}/groups/{groupId}/online")
    Integer getGroupMembersOnline(@PathVariable UUID systemUserId, @PathVariable String groupId) throws VKClientException {
        log.info("Request get group online");
        return groupService.getMembersOnlineOfGroup(systemUserId, groupId);
    }
}

