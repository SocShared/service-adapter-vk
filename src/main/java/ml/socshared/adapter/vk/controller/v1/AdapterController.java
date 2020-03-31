package ml.socshared.adapter.vk.controller.v1;

import javassist.NotFoundException;
import ml.socshared.adapter.vk.api.v1.rest.VKAdapterGroupAPI;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.domain.response.GroupResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.service.ApplicationService;
import ml.socshared.adapter.vk.vkclient.VKClient;
import ml.socshared.adapter.vk.vkclient.domain.Error;
import ml.socshared.adapter.vk.vkclient.domain.Paginator;
import ml.socshared.adapter.vk.vkclient.domain.VkGroup;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class AdapterController implements VKAdapterGroupAPI {

    private UUID userId;
    private int page;
    private int size;

    @Override
    @GetMapping("api/v1/users/{uuid}/info")
    public SystemUser getUsersInfo(@PathVariable(name="uuid") UUID id) throws NotFoundException {

//        try {
//            logger.info("GetUserInfo" + ids.toString());
//            VKClient vk = new VKClient( "92ad7907b2873cc6505ffc08068dda8cb0dcb7170b475a0ca5fba2d4a20e6a8ccaf72f77a0e95e674bc2");
//            List<User> res = vk.getUsersInfo(ids);
//            return res;
//        } catch(VKClientException exp) {
//            logger.error("Error", exp);
//            throw exp;
//        }

    return appService.getUser(id);
    }

    @Override
    @PostMapping("api/v1/users/{user}/app/{vk_app_id}")
    public String registerVkApp(@PathVariable(name = "user") UUID systemUserID,
                                  @PathVariable(name = "vk_app_id") String vkAppID) {
       appService.setApp(systemUserID, vkAppID);
        return "ok";
    }

    @Override
    @PostMapping("api/v1/users/{user}/app/access_token")
    public String setAccessToken(@PathVariable(name="user") UUID userId,
                                 @RequestBody String accessToken)
            throws NotFoundException {
        appService.setAccessToken(userId, accessToken);
        return "ok";
    }

//    @Override
//    @PostMapping("api/v1/users/{systemUserId}/groups/{GroupId}")
//    public void setGroup(@PathVariable("systemUserId") UUID userId,
//                         @PathVariable("GroupId")      String groupID,
//                         @RequestParam("isSelected")   boolean isSelected) throws NotFoundException {
//        if(isSelected == false) {
//            SystemUser su = appService.getUser(userId);
//            VKClient client = new VKClient(su.getAccessToken());
//            VkGroup g = client.getGroupInfo(groupID);
//            if(!su.getGroupVkId().equals(groupID)) {
//                return new InvalidRequest("this group not selected");
//            }
//            su.setGroupVkId("");
//            appService.updateUser(su);
//            GroupResponse response = new Group();
//            response.setType(g.getType());
//            response.setGroupId(groupID);
//            response.setMembersCount(g.);
//        }
//
//    }



    @Override
    @GetMapping("api/v1/users/{systemUserId}/groups")
    public Page<GroupResponse> getGroupsList
            (@PathVariable("systemUserId")                                 UUID userId,
             @RequestParam(name="page", required=false, defaultValue="1")  int page,
             @RequestParam(name="size", required=false, defaultValue="10") int size)
            throws NotFoundException, VKClientException {
        try {
            logger.info("Request get of Groups list");
            SystemUser sUser = appService.getUser(userId);
            VKClient client = new VKClient(sUser.getAccessToken());
            int offset = (page-1)*size;
            Paginator<VkGroup> groups =
                    client.getGroupsInfo(sUser.getVkUserId(), Arrays.asList("members_count", "description"),
                            Collections.singletonList("admin"), offset, size);
            Page<GroupResponse> response = new Page<>();
            response.setHasNext(groups.getCount() > (offset+size));
            response.setHasPrev(page > 1);

            List<GroupResponse> gl = new LinkedList<>();
            Iterator<VkGroup> itr = groups.getResponse().iterator();
            while(itr.hasNext()) {
                VkGroup vkGroup = itr.next();
                GroupResponse g = convertVkGroupToGroupDefault(vkGroup);
                g.setSystemUserId(userId);
                SystemUser u = appService.findUserByGroup(vkGroup.getId());
                boolean isSelected = (u != null) && (u.getId().equals(sUser.getId()));
                g.setSelected(isSelected);
                gl.add(g);
                if(isSelected) {
                    break;
                }
            }
            //мы уже нашли выбранную группу. Поскольку мы можем выбрать только одну группу, то остальные точно будут не выбранные
            //тогда нет смысла обращаться к СУБД
            while(itr.hasNext()) {
                VkGroup vkGroup = itr.next();
                GroupResponse g = convertVkGroupToGroupDefault(vkGroup);
                gl.add(g);
            }

            response.setObject(gl);
            response.setPage(page);
            response.setSize(response.getObject().size());
            return response;
        }
        catch(NotFoundException exp) {
            logger.info("User this uuid: " + userId + "not found");
            throw exp;
        }
        catch(VKClientException exp) {
            logger.info("Vk returned error object with code: " + exp.getError().getErrorCode() +
                    "(" + exp.getError().getErrorMsg() + ")" );
            throw exp;
        }

    }

    @Override
    @PostMapping("api/v1/users/{systemUserId}/groups/{groupId}")
    public GroupResponse setGroup(@PathVariable("systemUserId") UUID userId,
                         @PathVariable("groupId") String groupId,
                         @RequestParam("isSelected") boolean isSelected) throws NotFoundException, VKClientException {
        logger.info("Request of set group as selected");
        SystemUser sUser = appService.getUser(userId);
        VKClient client = new VKClient(sUser.getAccessToken());
        VkGroup group = null;
        GroupResponse g = null;
        if(isSelected) {
            group = client.getGroupInfo(groupId, Arrays.asList("members_count", "is_admin"));
            if(!group.getAdditionalFields().containsKey("is_admin")) {
                logger.warn("Response of vk don't containing was requested field is_admin");
                throw new VKClientException(new Error());
            }
            boolean is_selected= false;
            if("1".equals(group.getAdditionalFields().get("is_admin"))) {
                sUser.setGroupVkId(group.getId());
                is_selected = true;
            }
            appService.updateUser(sUser);
            g = convertVkGroupToGroupDefault(group);
            g.setSelected(is_selected);
        } else {
            if(!groupId.equals(sUser.getGroupVkId())) {
                logger.warn("User (" + userId + ") don't selected group (" + groupId + ")");
                throw new NotFoundException("group this id not found");
            }
            group = client.getGroupInfo(groupId, Arrays.asList("members_count", "is_admin"));
            if(!group.getAdditionalFields().containsKey("is_admin")) {
                logger.warn("Response of vk don't containing was requested field is_admin");
                throw new VKClientException(new Error());
            }
            boolean is_turn_selected= false;
            if("1".equals(group.getAdditionalFields().get("is_admin"))) {
                sUser.setGroupVkId(null);
                is_turn_selected = true;
            }
            appService.updateUser(sUser);
            g = convertVkGroupToGroupDefault(group);
            g.setSelected(!is_turn_selected);
        }

        return g;
    }

    @GetMapping("api/v1/users/{systemUserId}/groups/{groupId}")
    GroupResponse getGroupById(@PathVariable("systemUserId") UUID userId, @PathVariable("groupId") String groupId) throws NotFoundException, VKClientException {
        logger.info("Request of get group info by id");
        SystemUser sUser = appService.getUser(userId);
        VKClient client = new VKClient(sUser.getAccessToken());
        VkGroup group = client.getGroupInfo(groupId, Collections.singletonList("members_count"));
        GroupResponse response = convertVkGroupToGroupDefault(group);
        response.setSelected(response.getGroupId().equals(sUser.getGroupVkId()));
        return response;
    }



    static public GroupResponse convertVkGroupToGroupDefault(VkGroup vkGroup) {
        GroupResponse g = new GroupResponse();
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

    private Logger logger = LoggerFactory.getLogger(AdapterController.class);

    @Autowired
    private ApplicationService appService;
}

