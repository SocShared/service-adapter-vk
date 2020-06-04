package ml.socshared.adapter.vk.controller.v1;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.api.v1.rest.VkCallBackApi;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;



@Slf4j
@RestController
@RequestMapping("api/v1")
@PreAuthorize("isAuthenticated()")
public class VkCallBackApiController implements VkCallBackApi {


    @Override
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/private/users/{systemUserId}/callback/confirm")
    public String confirmService(@PathVariable UUID systemUserId,
                                                  @RequestBody HashMap<String, String> json) {
        log.info("Request for confirm CallBack Server");
        log.info("SystemUserID " + systemUserId);
        log.info("Json: " + json);
        return "fd8f9b41";
    }
}
