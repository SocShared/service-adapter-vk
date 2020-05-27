package ml.socshared.adapter.vk.controller.v1;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.api.v1.rest.VkCallBackApi;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RequestMapping("api/v1/")
@Slf4j
@RestController
public class VkCallBackApiController implements VkCallBackApi {


    @Override
    @PostMapping("users/{systemUserId}/callback/confirm")
    public String confirmService(@PathVariable UUID systemUserId,
                                                  @RequestBody HashMap<String, String> json) {
        log.info("Request for confirm CallBack Server");
        log.info("SystemUserID " + systemUserId);
        log.info("Json: " + json);
        return "fd8f9b41";
    }
}
