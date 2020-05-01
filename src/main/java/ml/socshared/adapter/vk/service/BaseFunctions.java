package ml.socshared.adapter.vk.service;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.exception.impl.HttpBadRequestException;

import ml.socshared.adapter.vk.vkclient.domain.Post;
import ml.socshared.adapter.vk.vkclient.domain.VkGroup;
import org.slf4j.Logger;

@Slf4j
public class BaseFunctions {

    public static void checkGroupConnectionToUser(String vkGroupId, SystemUser sUser, Logger log) {
        if(!vkGroupId.equals(sUser.getGroupVkId())) {
            String msg = "Group (" + vkGroupId + ") don't connect to user (" +
                    sUser.getId() + ")";
            log.warn(msg);
            throw new HttpBadRequestException(msg);
        }
    }

    public static void modifyOwnerId(Post post) {
        int owner = post.getOwnerId();
        if(owner < 0) {
            post.setOwnerId(-1*owner);
        }
    }

    public static void modifyGroupId(VkGroup g) {
        String owner = g.getId();
        if(owner.charAt(0) == '-') {
            g.setId(owner.substring(1));
        }
    }

}
