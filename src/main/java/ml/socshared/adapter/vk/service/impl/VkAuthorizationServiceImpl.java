package ml.socshared.adapter.vk.service.impl;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.exception.impl.HttpNotFoundException;
import ml.socshared.adapter.vk.exception.impl.VkAuthorizationException;
import ml.socshared.adapter.vk.service.ApplicationService;
import ml.socshared.adapter.vk.service.VkAuthorizationService;
import ml.socshared.adapter.vk.service.sentry.SentrySender;
import ml.socshared.adapter.vk.service.sentry.SentryTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class VkAuthorizationServiceImpl implements VkAuthorizationService {


    private ApplicationService appService;
    private SentrySender sentrySender;

    @Autowired
    VkAuthorizationServiceImpl(ApplicationService aps, SentrySender sentry)
    {
        appService = aps;
        sentrySender = sentry;
    }

    @Override
    public SystemUser getUser(UUID systemUserId) {
        try {
            SystemUser sUser = appService.getUser(systemUserId);
            if(sUser.getAccessToken() == null) {
                throw new VkAuthorizationException("User with uuid (" + systemUserId +
                        ") don't have a access token of vk app");
            }

            Map<String, Object> additional = new HashMap<>();
            sentrySender.sentryMessage("get user info", additional,
                    Collections.singletonList(SentryTag.GET_USER_INFO));

            return sUser;
        } catch(HttpNotFoundException exp) {
            log.warn("user this uuid ("+ systemUserId +") not found");
            throw exp;
        }
    }

}
