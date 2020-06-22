package ml.socshared.adapter.vk.controller.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.api.v1.rest.VkAdapterAppApi;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.domain.response.ApplicationCountResponse;
import ml.socshared.adapter.vk.domain.response.SocUserInfoResponse;
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
@RequestMapping(value = "api/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class VkAppController  implements VkAdapterAppApi {

    private final ApplicationService appService;

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
        } catch (HttpNotFoundException | VKClientException exp) {
            log.trace("user with id not found", exp);
            return null;
        }

    }

    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping(value = "/private/users/{user}/app/")
    public void appRegister(@PathVariable(name = "user") UUID systemUserID,
                         //   @PathVariable(name = "vk_app_id") String vkAppID,
                            @RequestBody String accessToken) throws HttpNotFoundException, VKClientException {
            String vkAppID = "NotRequired";//TODO нужен ли идентификатор приложения вк?
            log.info("Request of register vk application");
            appService.setApp(systemUserID, vkAppID, accessToken);

    }

    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/private/users/{systemUserId}/app/")
    public void appReset(@PathVariable UUID systemUserId) {
        log.info("Request of unregister vk application");
        appService.unsetApp(systemUserId);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/private/app/count")
    public ApplicationCountResponse appCount() {
        return appService.count();
    }

}
