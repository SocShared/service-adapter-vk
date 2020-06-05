package ml.socshared.adapter.vk.controller.v1;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.api.v1.rest.VkAdapterGroupApi;
import ml.socshared.adapter.vk.domain.response.GroupResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.exception.impl.HttpBadRequestException;
import ml.socshared.adapter.vk.exception.impl.HttpInternalServerErrorException;
import ml.socshared.adapter.vk.exception.impl.HttpNotFoundException;
import ml.socshared.adapter.vk.service.VkGroupService;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("api/v1")
@Slf4j
public class GroupController implements VkAdapterGroupApi {

    private VkGroupService groupService;

    @Autowired
    GroupController(VkGroupService groupService) {
        this.groupService = groupService;
    }

    @PreAuthorize("hasRole('SERVICE')")
   // @Override
    @GetMapping("/private/users/{systemUserId}/groups")
    public Page<GroupResponse> getGroupsList
            (@PathVariable("systemUserId")                                 UUID userId,
             @RequestParam(name="page", required=false, defaultValue="0")  int page,
             @RequestParam(name="size", required=false, defaultValue="10") int size)
            throws HttpNotFoundException{
        try {
            log.info("Request get of Groups list");
            return groupService.getUserGroups(userId, page, size);
        }
        catch(HttpNotFoundException exp) {
            log.warn("User this uuid: " + userId + "not found");
            throw exp;
        }
        catch(HttpBadRequestException exp) {
            log.warn("Invalid request parameters: " + exp.getMessage());
            throw exp;
        }
        catch(VKClientException exp) {
            log.info("Vk returned error object with code: " + exp.getErrorType().getErrorCode() +
                    "(" + exp.getErrorType().getErrorMsg() + ")" );
            throw new HttpInternalServerErrorException("Vk returned error: " + exp.getErrorType().getErrorMsg() + "(" + exp.getErrorType().getErrorCode() + ")");
        }

    }

    @PreAuthorize("hasRole('SERVICE')")
    @Override
    @PostMapping("/private/users/{systemUserId}/groups/{groupId}")
    public GroupResponse setGroup(@PathVariable("systemUserId") UUID userId,
                         @PathVariable("groupId") String groupId,
                         @RequestParam("isSelected") boolean isSelected)   {
        log.info("Request of set group as selected");
        try{
            return groupService.selectGroup(userId, groupId, isSelected);
        } catch (VKClientException e) {
            log.warn("VkClient Error: " + e.getMessage());
            throw new HttpInternalServerErrorException(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{systemUserId}/groups/{groupId}")
    GroupResponse getGroupById(@PathVariable UUID systemUserId, @PathVariable String groupId) {
        log.info("Request of get group info by id");
        try {
            return groupService.getUserGroupbyId(systemUserId, groupId);
        } catch (VKClientException e) {
            log.warn("VkClient Error:" + e.getMessage());
            throw new HttpInternalServerErrorException(e.getMessage());
        }
    }
}

