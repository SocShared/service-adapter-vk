package ml.socshared.adapter.vk.api.v1.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;
import ml.socshared.adapter.vk.domain.db.SystemUser;

import java.util.UUID;

@Api(value="Connection Vkontakte standalone-application")
public interface VkAdapterAppApi {

    @ApiOperation(value="Obtaining the identifier of the connected standalone application and its access token ")
    @ApiResponses(value={
            @ApiResponse(code = 200, message = "Successful! Will returned json of user information"),
            @ApiResponse(code=404, message = "When user with uuid not found")
    })
    SystemUser getUsersInfo(UUID systemUserId) throws NotFoundException;

    @ApiOperation(value = "", notes="add application id with access token")
    @ApiResponses(value = {
            @ApiResponse(code=200, message = "Successful!"),
            @ApiResponse(code=404, message = "When user with uuid not found")
            })
    void appRegister(UUID systemUserId, String vkAppId, String accessToken);
}
