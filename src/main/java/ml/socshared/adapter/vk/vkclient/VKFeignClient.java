package ml.socshared.adapter.vk.vkclient;


import feign.Param;
import feign.RequestLine;
import ml.socshared.adapter.vk.vkclient.config.ClientConfiguration;
import ml.socshared.adapter.vk.vkclient.domain.*;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import java.util.Map;

@FeignClient(name="VKFeignClient",
             configuration= ClientConfiguration.class)
public interface VKFeignClient {

    @RequestLine("GET /users.get?user_ids={ids}&access_token={token}&v=105.3")
    VKResponse<List<User>> getUsersInfo(@Param(value ="ids") String ids,
                                        @Param(value ="token")String token);

    @RequestLine("GET /users.get?access_token={token}&v=103.5")
    VKResponse<List<User>> getCurrentUserInfo(@Param(value ="token")String token);

    @RequestLine("GET /groups.get?fields={fields}&extended={extended}" +
            "&filter={filter}&access_token={token}&offset={offset}&count={count}&v=105.3")
    VKResponse<Paginator<VkGroup>> getGroupsInfo( @Param("fields") String field,
                                                 @Param("extended")int extended, @Param("filter") String filter,
                                                 @Param("token") String accessToken, @Param("offset") int offset,
                                                 @Param("count") int count);

    @RequestLine("GET /groups.getById?group_ids={group_id}&fields={fields}&access_token={token}&v=103.5")
    VKResponse<List<VkGroup>> getGroupInfo(@Param("group_id") String groupID, @Param("fields") String fields,
                                     @Param("token") String token);

    @RequestLine("GET /wall.get?owner_id={owner_id}&offset={offset}&count={count}&filter={filter}" +
                "&access_token={token}&v=103.5")
    VKResponse<Paginator<Post>> getPosts(@Param("owner_id") String ownerId, @Param("offset") int offset,
                                    @Param("count") int count, @Param("filter") String filter,
                                         @Param("token") String token);

    @RequestLine("GET /wall.getById?posts={extendPostId}&access_token={token}&v=103.5")
    VKResponse<List<Post>> getPost(@Param("extendPostId") String extendPostId,
                                         @Param("token") String token);

    @RequestLine("GET /wall.post?owner_id={groupId}&from_group=1&message={message}&close_comments=0&mute_notifications=0" +
            "&access_token={token}&v=103.5")
    VKResponse<Map<String, String>> sendPost(@Param("groupId")String groupId,@Param("message") String message,
                                             @Param("token") String token);

    @RequestLine("GET /wall.edit?owner_id={groupId}&post_id={post_id}&from_group=1&message={message}" +
            "&close_comments=0&mute_notifications=0&access_token={token}&v=103.5")
    VKResponse<Map<String, String>> editPost(@Param("groupId")String groupId, @Param("post_id") String postId,
                                             @Param("message") String message, @Param("token") String token);


    @RequestLine("GET /wall.delete?owner_id={groupId}&post_id={post_id}&access_token={token}&v=103.5")
    VKResponse<String> deletePost(@Param("groupId")String groupId, @Param("post_id") String postId,
                                  @Param("token") String token);

    @RequestLine("GET /wall.getComment?owner_id={ownerId}&comment_id={commentId}&access_token={token}&v=103.5")
    VKResponse<List<VkComment>> getCommentPostById(@Param("ownerId") String ownerId, @Param("commentId") String commentId,
                                             @Param("token") String token);

    @RequestLine("GET /wall.getComments?owner_id={ownerId}&post_id={postId}&need_likes=1&offset={offset}&count={count}" +
            "&access_token={token}&v=103.5")
    VKResponse<Paginator<VkComment>> getCommentsPost(@Param("ownerId") String ownerId, @Param("postId") String postId,
                                                     @Param("offset") int offset, @Param("count") int count,
                                                     @Param("token") String token);
    @RequestLine("GET /wall.getComments?owner_id={ownerId}&post_id={postId}&comment_id={commentId}" +
            "&need_likes=1&offset={offset}&count={count}&access_token={token}&v=103.5")
    VKResponse<Paginator<VkSubComment>> getSubComments(@Param("ownerId") String ownerId, @Param("postId") String postId,
                                                       @Param("commentId") String superCommentId,
                                                       @Param("offset") int offset, @Param("count") int count,
                                                       @Param("token") String token);
    //comment_id

 }
