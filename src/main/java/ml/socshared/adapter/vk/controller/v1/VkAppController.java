package ml.socshared.adapter.vk.controller.v1;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.api.v1.rest.VkAdapterAppApi;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.domain.response.SocUserInfoResponse;
import ml.socshared.adapter.vk.exception.impl.HttpInternalServerErrorException;
import ml.socshared.adapter.vk.exception.impl.HttpNotFoundException;
import ml.socshared.adapter.vk.service.ApplicationService;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/v1")
public class VkAppController  implements VkAdapterAppApi {

    private ApplicationService appService;

    @Autowired
    VkAppController(ApplicationService app) {
        appService = app;
    }


    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{uuid}/info")
    public SystemUser getUsersInfo(@PathVariable(name="uuid") UUID id) throws HttpNotFoundException {
        log.info("Request of get user info by id: " + id);
        return appService.getUser(id);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/users/{uuid}/vk/data")
    SocUserInfoResponse getSocUserInfo(@PathVariable UUID uuid) {
        log.info("Request get social user info");
        try{
            return appService.getUserSocialInformation(uuid);
        }catch (VKClientException e) {
            String msg = "VkClien Error: " + e.getMessage() + "(Code: " + e.getErrorType() + ")";
            log.warn(msg);
            throw new HttpInternalServerErrorException(msg);
        } catch (HttpNotFoundException exp) {
            log.trace("user with id not found", exp);
            return null;
        }

    }

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping(value = "/private/users/{user}/app/",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void appRegister(@PathVariable(name = "user") UUID systemUserID,
                         //   @PathVariable(name = "vk_app_id") String vkAppID,
                            @RequestBody String accessToken) throws HttpNotFoundException {
        try {
            String vkAppID = "NotRequired";//TODO нужен ли идентификатор приложения вк?
            log.info("Request of register vk application");
            appService.setApp(systemUserID, vkAppID, accessToken);
        } catch (VKClientException e) {
            String msg = "VkClien Error: " + e.getMessage() + "(Code: " + e.getErrorType() + ")";
            log.warn(msg);
            throw new HttpInternalServerErrorException(msg);
        }

    }

    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/private/users/{systemUserId}/app/")
    public void appReset(@PathVariable UUID systemUserId) {
        log.info("Request of unregister vk application");
        appService.unsetApp(systemUserId);
    }

}
