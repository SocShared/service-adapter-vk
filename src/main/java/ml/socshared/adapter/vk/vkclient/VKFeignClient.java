package ml.socshared.adapter.vk.vkclient;


import feign.Param;
import feign.RequestLine;
import ml.socshared.adapter.vk.vkclient.config.ClientConfiguration;
import ml.socshared.adapter.vk.vkclient.domain.*;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(name="VKFeignClient",
             configuration= ClientConfiguration.class)
public interface VKFeignClient {

    @RequestLine("GET /users.get?user_ids={ids}&access_token={token}&v=105.3")
    VKResponse<List<User>> getUsersInfo(@Param(value ="ids") String ids,
                                        @Param(value ="token")String token);

    @RequestLine("GET /groups.get?fields={fields}&extended={extended}" +
            "&filter={filter}&access_token={token}&offset={offset}&count={count}&v=105.3")
    VKResponse<Paginator<VkGroup>> getGroupsInfo( @Param("fields") String field,
                                                 @Param("extended")int extended, @Param("filter") String filter,
                                                 @Param("token") String accessToken, @Param("offset") int offset,
                                                 @Param("count") int count);

    @RequestLine("GET /groups.getById?group_id={group_id}&fields={fields}&access_token={token}&v=103.5")
    VKResponse<List<VkGroup>> getGroupInfo(@Param("group_id") String groupID, @Param("fields") String fields,
                                     @Param("token") String token);

    @RequestLine("GET /wall.get?owner_id={owner_id}&offset={offset}&count={count}&filter={filter}" +
                "&access_token={token}&v=103.5")
    VKResponse<Paginator<Post>> getPosts(@Param("owner_id") String ownerId, @Param("offset") int offset,
                                    @Param("count") int count, @Param("filter") String filter,
                                         @Param("token") String token);

 }
