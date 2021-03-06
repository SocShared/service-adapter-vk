package ml.socshared.adapter.vk.vkclient;

import feign.Feign;
import feign.form.FormEncoder;
import lombok.extern.slf4j.Slf4j;
import ml.socshared.adapter.vk.vkclient.config.ClientConfiguration;
import ml.socshared.adapter.vk.vkclient.domain.*;
import ml.socshared.adapter.vk.vkclient.exception.InvalidParameterException;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class VKClient {

    private VKFeignClient client;
    private String token;
    final private String vkApiVersion = "103.5";
    private Long timer = Clock.systemUTC().millis();
    private Long VK_TIME_PERIOD_LIMIT = 340L;

    @Autowired
    ObjectFactory<HttpMessageConverters> messageConverters;

    private void applyDelay(Long delay) {
        Long targetTime = Clock.systemUTC().millis() + delay;
        while (Clock.systemUTC().millis() < targetTime) {}
    }

    private void requestSpeedLimiter() {
        Long targetTime = timer + VK_TIME_PERIOD_LIMIT;
        Long currentTime = Clock.systemUTC().millis();

        if(currentTime < targetTime) {
            Long delay = targetTime - currentTime;
            applyDelay(delay);
            log.warn("VK SPEED LIMITER; delay = {} ms", delay);
        }
        timer = Clock.systemUTC().millis();
    }

   public VKClient() {
       ClientConfiguration config = new ClientConfiguration();
       // this.token = accessToken;

        this.client = Feign.builder()
                .decoder(config.getDecoder())
                .encoder(new FormEncoder(new SpringEncoder(this.messageConverters)))
                .target(VKFeignClient.class, "https://api.vk.com/method");
    }
    public void setToken(String token) {
       this.token = token;
    }

    public User getCurrentUserInfo() throws VKClientException {
        requestSpeedLimiter();

       VKResponse<List<User>> res =  client.getCurrentUserInfo(token);
       if(res.isError()) {
           throw new VKClientException(res.getError());
       }
       return res.getResponse().get(0);
    }

    public List<User> getUsersInfo(List<Integer> ids) throws VKClientException {
        requestSpeedLimiter();

        StringBuilder idsParam = new StringBuilder();
        if(ids.size() == 0) {
            throw new InvalidParameterException("Ids must be required parameter. Not be empty");
        } else if(ids.size() == 1) {
            idsParam.append(ids.get(0));
        } else {
            for(int i = 0; i < ids.size()-1; i++) {
                idsParam.append(ids.get(i));
                idsParam.append(",");
            }
            idsParam.append(ids.get(ids.size()-1));
        }

        VKResponse<List<User>> response = client.getUsersInfo(idsParam.toString(), token);
        if(response.isError()) {
            throw new VKClientException("vk error", response.getError());
        }
        return response.getResponse();
    }

    public VkGroup getGroupInfo(String groupId, List<String> fields) throws VKClientException {
        requestSpeedLimiter();

       StringBuilder fieldsList = convertListStringToString(fields);
       VKResponse<List<VkGroup>> response = client.getGroupInfo(groupId, fieldsList.toString(), token);
       if(response.isError() || response.getResponse().size() == 0) {
           throw new VKClientException(response.getError());
       } else {
           return response.getResponse().get(0);
       }
    }

    public Paginator<VkGroup> getGroupsInfo(String userId, List<String> fields,
                                            List<String> filter, int offset, int count) throws VKClientException {
        requestSpeedLimiter();

        checkOffsetSize(offset, count);
       StringBuilder fieldsList = convertListStringToString(fields);
        StringBuilder filtersList = convertListStringToString(filter);
       VKResponse<Paginator<VkGroup>> response = client.getGroupsInfo(fieldsList.toString(),
                                                                     1,
                                                                     filtersList.toString(), token,
                                                                     offset, count);
       if(response.isError()) {
           throw new VKClientException(response.getError());
       }
       return response.getResponse();
    }

    public Paginator<Post> getPostsOfGroup(String groupId, String filter, int offset, int count) throws VKClientException {
        requestSpeedLimiter();

        checkOffsetSize(offset, count);
       VKResponse<Paginator<Post>> response = client.getPosts("-"+groupId, offset, count, filter, token);
       if(response.isError()) {
           throw new VKClientException(response.getError());
       } else {
           Paginator<Post> vkGroup = response.getResponse();

           return response.getResponse();
       }
    }

    public Post getPostOfGroup(String groupId, String postId) throws VKClientException {
        requestSpeedLimiter();

        VKResponse<List<Post>> response = client.getPost("-"+groupId + "_" + postId, token);
        if(response.isError()) {
            throw new VKClientException(response.getError());
        } else {
            if(response.getResponse().size() == 0) {
                throw new VKClientException(new ErrorType(ErrorCode.UNDEFINED_ERROR));
            }
            return response.getResponse().get(0);
        }
    }

    public String sendPostToGroup(String vkGroupId, String message) throws VKClientException {
        requestSpeedLimiter();

        Map<String, String> params = new HashMap<>();
        params.put("owner_id", "-" + vkGroupId);
        params.put("message", message);
        params.put("close_comments", "0");
        params.put("v", "103.5");
        params.put("access_token", token);
        params.put("from_group", "1");
       VKResponse<Map<String, String>> result = client.sendPost(params);
       if(result.isError()) {
           throw new VKClientException(result.getError());
       } else {
           return result.getResponse().getOrDefault("post_id", "");
       }
    }

    public String editPostToGroup(String vkGroupId,String postId, String message) throws VKClientException {
        requestSpeedLimiter();

        VKResponse<Map<String, String>> result = client.editPost("-"+vkGroupId, postId,message,  token);
        if(result.isError()) {
            throw new VKClientException(result.getError());
        } else {
            return result.getResponse().getOrDefault("post_id", "");
        }
    }

    public void deletePost(String vkGroupId, String postId) throws VKClientException {
        requestSpeedLimiter();

       VKResponse<String> result = client.deletePost('-' + vkGroupId, postId, token);
        if(result.isError()) {
            throw new VKClientException(result.getError());
        }
    }

    public VkComment getCommentById(String vkGroupID, String commentId) throws VKClientException {
        requestSpeedLimiter();

        VKResponse<List<VkComment>> response = client.getCommentPostById("-" + vkGroupID, commentId, token);
        if(response.isError()) {
            throw new VKClientException(response.getError());
        }
        return response.getResponse().get(0);
    }

    public Paginator<VkComment> getCommentsOfPost(String vkGroupId, String postId, int offset, int count) throws VKClientException {
        requestSpeedLimiter();

        VKResponse<Paginator<VkComment>>  response =
               client.getCommentsPost("-" + vkGroupId, postId,  offset, count, token);
        if(response.isError()) {
            throw new VKClientException(response.getError());
        }
        return response.getResponse();
    }
    public Paginator<VkSubComment> getSubComments(String vkGroupId, String postId, String superCommentId, int offset, int count) throws VKClientException {
        requestSpeedLimiter();

       VKResponse<Paginator<VkSubComment>> response = client.getSubComments("-" + vkGroupId, postId, superCommentId,
                                                     offset, count, token);
        if(response.isError()) {
            throw new VKClientException(response.getError());
        }
        return response.getResponse();

    }

    public Integer getGroupOnline(String vkGroupId) throws VKClientException {
        requestSpeedLimiter();

        VKResponse<VkOnline> response = client.getGroupOnline(vkGroupId, token);
        if(response.isError()) {
            throw new VKClientException(response.getError());
        }
        return response.getResponse().getOnline();
    }

    private static StringBuilder convertListStringToString(List<String> list) {

       StringBuilder str = new StringBuilder();
       for(int i = 0; i < list.size()-1; i++) {
            str.append(list.get(i));
            str.append(',');
        }
        str.append(list.get(list.size()-1));
       return str;
    }

        public void checkOffsetSize(int offset, int count) throws VKClientException {
            if(offset < 0) {
                throw new VKClientException("argument a offset can't be less then 0", new ErrorType(ErrorCode.INVALID_ARGUMENTS));
            }
            if(count < 0) {
                throw new VKClientException("argument a count cannot be less then 0", new ErrorType(ErrorCode.INVALID_ARGUMENTS));
            }
            if(count > 100) {
                throw new VKClientException("argument a count cannot be more then 100", new ErrorType(ErrorCode.INVALID_ARGUMENTS));
            }
        }



}
