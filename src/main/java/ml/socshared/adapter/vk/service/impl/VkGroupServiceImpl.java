package ml.socshared.adapter.vk.service.impl;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.domain.response.GroupResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.exception.impl.HttpBadRequestException;
import ml.socshared.adapter.vk.exception.impl.HttpNotFoundException;
import ml.socshared.adapter.vk.service.ApplicationService;
import ml.socshared.adapter.vk.service.BaseFunctions;
import ml.socshared.adapter.vk.service.VkAuthorizationService;
import ml.socshared.adapter.vk.service.VkGroupService;
import ml.socshared.adapter.vk.vkclient.VKClient;
import ml.socshared.adapter.vk.vkclient.domain.ErrorType;
import ml.socshared.adapter.vk.vkclient.domain.Paginator;
import ml.socshared.adapter.vk.vkclient.domain.VkGroup;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class VkGroupServiceImpl implements VkGroupService {

    private VkAuthorizationService vkAuth;
    private ApplicationService appService;
    private VKClient client;

    @Autowired
    VkGroupServiceImpl(VkAuthorizationService auth, VKClient client, ApplicationService app) {
        this.vkAuth = auth;
        this.client = client;
        this.appService = app;
    }


    @Override
    public Page<GroupResponse> getUserGroups(UUID systemUserId, int page, int size) throws VKClientException {
        if(page < 0) {
            throw new HttpBadRequestException("value of page must be above zero");
        }
        if((size < 0)) {
            throw new HttpBadRequestException("value of size must be above 0");
        } else if (size > 100) {
            throw new HttpBadRequestException("value of size must be less than 100");
        }


        SystemUser sUser = vkAuth.getUser(systemUserId);
        client.setToken(sUser.getAccessToken());
        Paginator<VkGroup> vkGroups =  client.getGroupsInfo(sUser.getVkUserId(),  Arrays.asList("members_count", "description"),
                Collections.singletonList("admin"), (page-1)*size, size);
        List<GroupResponse> response = new LinkedList<>();
        for(VkGroup group : vkGroups.getResponse()) {
            GroupResponse g = convertVkGroupToGroupResponseDefault(group);
            boolean isSelected = group.getId().equals(sUser.getGroupVkId());
            g.setSelected(isSelected);
        }
        Page<GroupResponse> responsePage = new Page<>();
        responsePage.setSize(response.size());
        responsePage.setPage(page);
        responsePage.setHasPrev(page>1);
        responsePage.setHasNext(page*size < vkGroups.getCount());
        responsePage.setObject(response);
        return responsePage;
    }

    @Override
    public GroupResponse getUserGroupbyId(UUID systemUserId, String vkGroupId) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);
        client.setToken(sUser.getAccessToken());
        VkGroup group = client.getGroupInfo(vkGroupId, Collections.singletonList("members_count"));
        GroupResponse response = convertVkGroupToGroupResponseDefault(group);
        response.setSelected(response.getGroupId().equals(sUser.getGroupVkId()));
        return response;
    }

    @Override
    public GroupResponse selectGroup(UUID systemUserId, String vkGroupId, boolean isSelected) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);
        client.setToken(sUser.getAccessToken());
        VkGroup group = null;
        GroupResponse g = null;
        if(isSelected) {
            group = client.getGroupInfo(vkGroupId, Arrays.asList("members_count", "is_admin"));
            if(!group.getAdditionalFields().containsKey("is_admin")) {
                String msg = "Response of vk don't containing was requested field is_admin";
                log.warn(msg);
                throw new VKClientException(msg,new ErrorType());
            }
            boolean is_selected= false;
            if("1".equals(group.getAdditionalFields().get("is_admin"))) {
                sUser.setGroupVkId(group.getId());
                is_selected = true;
            }  else {
                //TODO если пришел запрос на добавление в  выбранные группы, которы не являются пользователь админ то это ошибка
                throw new HttpBadRequestException("You don't connect group where you not have admin role");
            }
            appService.updateUser(sUser);
            g = convertVkGroupToGroupResponseDefault(group);
            g.setSelected(is_selected);
        } else {
            if(!vkGroupId.equals(sUser.getGroupVkId())) {
                log.warn("User (" + systemUserId + ") don't selected group (" + vkGroupId + ")");
                throw new HttpNotFoundException("group this id not found");
            }
            group = client.getGroupInfo(vkGroupId, Arrays.asList("members_count", "is_admin"));
            if(!group.getAdditionalFields().containsKey("is_admin")) {
                log.warn("Response of vk don't containing was requested field is_admin");
                throw new VKClientException(new ErrorType());
            }
            boolean is_turn_selected= false;
            if("1".equals(group.getAdditionalFields().get("is_admin"))) {
                sUser.setGroupVkId(null);
                is_turn_selected = true;
            }
            appService.updateUser(sUser);
            g = convertVkGroupToGroupResponseDefault(group);
            g.setSelected(!is_turn_selected);
        }

        return g;
    }

    static public GroupResponse convertVkGroupToGroupResponseDefault(VkGroup vkGroup) {
        GroupResponse g = new GroupResponse();
        BaseFunctions.modifyGroupId(vkGroup);
        g.setGroupId(vkGroup.getId());
        g.setType(vkGroup.getType());
        g.setName(vkGroup.getName());
        g.setSelected(false);
        Map<String, String> additional = vkGroup.getAdditionalFields();
        if (additional.containsKey("members_count")) {
            g.setMembersCount(Integer.parseInt(additional.get("members_count")));
        }
        return g;
    }
}
