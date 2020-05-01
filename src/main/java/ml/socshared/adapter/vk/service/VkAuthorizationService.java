package ml.socshared.adapter.vk.service;

import ml.socshared.adapter.vk.domain.db.SystemUser;

import java.util.UUID;

public interface VkAuthorizationService {
   SystemUser getUser(UUID systemUserId);
}
