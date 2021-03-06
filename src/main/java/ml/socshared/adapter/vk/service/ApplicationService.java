package ml.socshared.adapter.vk.service;


import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.domain.response.ApplicationCountResponse;
import ml.socshared.adapter.vk.domain.response.SocUserInfoResponse;
import ml.socshared.adapter.vk.exception.impl.HttpNotFoundException;
import ml.socshared.adapter.vk.repository.SystemUserRepository;
import ml.socshared.adapter.vk.vkclient.VKClient;
import ml.socshared.adapter.vk.vkclient.domain.User;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationService {


    @Autowired
    public ApplicationService(SystemUserRepository sur, VKClient client) {

        this.usersRepository = sur;
        this.client = client;
    }



    public void setApp(UUID userId, String appVkId, String accessToken) throws VKClientException {
        Optional<SystemUser> userOptional = usersRepository.findById(userId);
        SystemUser user = null;
        if(userOptional.isEmpty()) {
            user = new SystemUser();
            user.setId(userId);
        } else {
            user = userOptional.get();
        }
        client.setToken(accessToken);
       // User info = client.getCurrentUserInfo();
       // user.setVkUserId(String.valueOf(info.getId()));
        //user.setGroupVkId(appVkId);
        user.setAccessToken(accessToken);
        usersRepository.save(user);
    }

    public void unsetApp(UUID systemUserId) {
        Optional<SystemUser> userOptional = usersRepository.findById(systemUserId);
        if(userOptional.isEmpty()) {
            throw new HttpNotFoundException("user with id " + systemUserId + " not found");
        }
       usersRepository.delete(userOptional.get());
    }


    public void setGroupId(UUID userId, String groupVkId) throws HttpNotFoundException {
        SystemUser su = getUser(userId);
        usersRepository.save(su);
    }

    public SystemUser getUser(UUID userID) throws HttpNotFoundException {
        Optional<SystemUser> userOptions = usersRepository.findById(userID);
        if(userOptions.isEmpty()) {
            throw new HttpNotFoundException("User this uuid (+" + userID + "+) not found");
        }
        return userOptions.get();
    }

    public void updateUser(SystemUser su) {
        usersRepository.save(su);
    }


    public void setAccessToken(UUID userId, String accessTokenVk) throws HttpNotFoundException {
        SystemUser user = getUser(userId);
        user.setAccessToken(accessTokenVk);
        usersRepository.save(user);
    }
    public SocUserInfoResponse getUserSocialInformation(UUID systemUserId) throws VKClientException {
        SystemUser user = getUser(systemUserId);
        client.setToken(user.getAccessToken());
        User vkUser = client.getCurrentUserInfo();
        SocUserInfoResponse response = new SocUserInfoResponse();
        response.setAccountId(vkUser.getId());
        response.setFirstName(vkUser.getFirst_name());
        response.setLastName(vkUser.getLast_name());
        response.setSystemUserId(systemUserId);
        return response;
    }

    public ApplicationCountResponse count() {
        return ApplicationCountResponse.builder()
                .count(usersRepository.count())
                .build();
    }

    private SystemUserRepository usersRepository;
    private VKClient client;
}
