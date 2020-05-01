package ml.socshared.adapter.vk.api.v1.rest;

import io.swagger.annotations.Api;
import javassist.NotFoundException;
import ml.socshared.adapter.vk.domain.response.GroupResponse;
import ml.socshared.adapter.vk.domain.response.Page;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;

import java.util.UUID;

@Api(value = "Api for working with vk groups through Adapter")
public interface VkAdapterGroupApi {
    Page<GroupResponse> getGroupsList(UUID userId, int page, int size) throws NotFoundException, VKClientException;
    GroupResponse setGroup(UUID userId, String groupID, boolean isSelected) throws NotFoundException, VKClientException;
}
