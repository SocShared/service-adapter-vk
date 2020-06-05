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
import ml.socshared.adapter.vk.service.sentry.SentrySender;
import ml.socshared.adapter.vk.service.sentry.SentryTag;
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
    private SentrySender sentrySender;

    @Autowired
    VkGroupServiceImpl(VkAuthorizationService auth, VKClient client, ApplicationService app,
                       SentrySender sentry) {
        this.vkAuth = auth;
        this.client = client;
        this.appService = app;
        this.sentrySender = sentry;
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
                Collections.singletonList("admin"), page*size, size);
        List<GroupResponse> response = new LinkedList<>();
        for(VkGroup group : vkGroups.getResponse()) {
            GroupResponse g = convertVkGroupToGroupResponseDefault(group);
            g.setSystemUserId(systemUserId);
            response.add(g);
        }
        Page<GroupResponse> responsePage = new Page<>();
        responsePage.setSize(response.size());
        responsePage.setPage(page);
        responsePage.setHasPrev(page>0);
        responsePage.setHasNext(page*size < vkGroups.getCount());
        responsePage.setObject(response);

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_user_id", systemUserId);
        sentrySender.sentryMessage("get user vk groups", additional,
                Collections.singletonList(SentryTag.GetUserGroups));


        return responsePage;
    }

    @Override
    public GroupResponse getUserGroupbyId(UUID systemUserId, String vkGroupId) throws VKClientException {
        SystemUser sUser = vkAuth.getUser(systemUserId);
        client.setToken(sUser.getAccessToken());
        VkGroup group = client.getGroupInfo(vkGroupId, Collections.singletonList("members_count"));
        GroupResponse response = convertVkGroupToGroupResponseDefault(group);
        //response.setSelected(response.getGroupId().equals(sUser.getGroupVkId()));

        Map<String, Object> additional = new HashMap<>();
        additional.put("system_user_id", systemUserId);
        additional.put("group_id", systemUserId);
        sentrySender.sentryMessage("get user's group info by id", additional,
                Collections.singletonList(SentryTag.GetUserGroup));


        return response;
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
