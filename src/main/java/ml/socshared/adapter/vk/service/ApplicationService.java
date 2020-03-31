package ml.socshared.adapter.vk.service;


import javassist.NotFoundException;
import ml.socshared.adapter.vk.domain.db.SystemUser;

import ml.socshared.adapter.vk.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationService {


    @Autowired
    public ApplicationService(SystemUserRepository sur) {
        usersRepository = sur;
    }

    public SystemUser findUserByGroup(String groupId) {
        return usersRepository.findByGroupVkId(groupId);
    }

    public void setApp(UUID userId, String appVkId) {
        Optional<SystemUser> userOptional = usersRepository.findById(userId);
        SystemUser user = null;
        if(userOptional.isEmpty()) {
            user = new SystemUser();
            user.setId(userId);
        }
        user.setAccessToken(appVkId);
        usersRepository.save(user);
    }

    public void setGroupId(UUID userId, String groupVkId) throws NotFoundException {
        SystemUser su = getUser(userId);
        su.setGroupVkId(groupVkId);
        usersRepository.save(su);
    }

    public SystemUser getUser(UUID userID) throws NotFoundException {
        Optional<SystemUser> userOptions = usersRepository.findById(userID);
        if(userOptions.isEmpty()) {
            throw new NotFoundException("user this uuid not found");
        }
        return userOptions.get();
    }

    public void updateUser(SystemUser su) {
        usersRepository.save(su);
    }


    public void setAccessToken(UUID userId, String accessTokenVk) throws NotFoundException {
        SystemUser user = getUser(userId);
        user.setAccessToken(accessTokenVk);
        usersRepository.save(user);
    }
    private SystemUserRepository usersRepository;
}
