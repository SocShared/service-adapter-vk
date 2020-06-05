package ml.socshared.adapter.vk.service;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.vkclient.domain.Post;
import ml.socshared.adapter.vk.vkclient.domain.VkGroup;

@Slf4j
public class BaseFunctions {

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
