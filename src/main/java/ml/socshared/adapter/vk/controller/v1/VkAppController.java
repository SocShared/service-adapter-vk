package ml.socshared.adapter.vk.controller.v1;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.api.v1.rest.VkAdapterAppApi;
import ml.socshared.adapter.vk.domain.db.SystemUser;
import ml.socshared.adapter.vk.exception.impl.HttpNotFoundException;
import ml.socshared.adapter.vk.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("api/v1")
public class VkAppController  implements VkAdapterAppApi {

    private ApplicationService appService;

    @Autowired
    VkAppController(ApplicationService app) {
        appService = app;
    }


    @Override
    @GetMapping("/users/{uuid}/info")
    public SystemUser getUsersInfo(@PathVariable(name="uuid") UUID id) throws HttpNotFoundException {
        log.info("Request of get user info by id: " + id);
        return appService.getUser(id);
    }

    @Override
    @PostMapping("/users/{user}/app/{vk_app_id}")
    public void appRegister(@PathVariable(name = "user") UUID systemUserID,
                            @PathVariable(name = "vk_app_id") String vkAppID,
                            @RequestBody String accessToken) throws HttpNotFoundException {
        log.info("Request of register vk application");
        appService.setApp(systemUserID, vkAppID, accessToken);
    }

}
