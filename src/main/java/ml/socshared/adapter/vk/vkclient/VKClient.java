package ml.socshared.adapter.vk.vkclient;

import feign.Feign;
import ml.socshared.adapter.vk.vkclient.config.ClientConfiguration;
import ml.socshared.adapter.vk.vkclient.domain.*;
import ml.socshared.adapter.vk.vkclient.exception.InvalidParameterException;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;

import java.util.List;
import java.util.Map;


public class VKClient {

    private VKFeignClient client;
    private String token;

   public VKClient() {
       ClientConfiguration config = new ClientConfiguration();
       // this.token = accessToken;
        this.client = Feign.builder()
                .decoder(config.getDecoder())
                //.encoder(config.getEncoder())
                .target(VKFeignClient.class, "https://api.vk.com/method");
    }
    public void setToken(String token) {
       this.token = token;
    }

    public List<User> getUsersInfo(List<Integer> ids) throws VKClientException {
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
       StringBuilder fieldsList = convertListStringToString(fields);
       VKResponse<List<VkGroup>> response = client.getGroupInfo("-"+groupId, fieldsList.toString(), token);
       if(response.isError() || response.getResponse().size() == 0) {
           throw new VKClientException(response.getError());
       } else {
           return response.getResponse().get(0);
       }
    }

    public Paginator<VkGroup> getGroupsInfo(String userId, List<String> fields,
                                            List<String> filter, int offset, int count) throws VKClientException {
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
       VKResponse<Map<String, String>> result = client.sendPost("-"+vkGroupId, message,  token);
       if(result.isError()) {
           throw new VKClientException(result.getError());
       } else {
           return result.getResponse().getOrDefault("post_id", "");
       }
    }

    public String editPostToGroup(String vkGroupId,String postId, String message) throws VKClientException {
        VKResponse<Map<String, String>> result = client.editPost("-"+vkGroupId, postId,message,  token);
        if(result.isError()) {
            throw new VKClientException(result.getError());
        } else {
            return result.getResponse().getOrDefault("post_id", "");
        }
    }

    public void deletePost(String vkGroupId, String postId) throws VKClientException {
       VKResponse<String> result = client.deletePost('-' + vkGroupId, postId, token);
        if(result.isError()) {
            throw new VKClientException(result.getError());
        }
    }

    public VkComment getCommentById(String vkGroupID, String commentId) throws VKClientException {
        VKResponse<List<VkComment>> response = client.getCommentPostById("-" + vkGroupID, commentId, token);
        if(response.isError()) {
            throw new VKClientException(response.getError());
        }
        return response.getResponse().get(0);
    }

    public Paginator<VkComment> getCommentsOfPost(String vkGroupId, String postId, int offset, int count) throws VKClientException {
       VKResponse<Paginator<VkComment>>  response =
               client.getCommentsPost("-" + vkGroupId, postId,  offset, count, token);
        if(response.isError()) {
            throw new VKClientException(response.getError());
        }
        return response.getResponse();
    }
    public Paginator<VkSubComment> getSubComments(String vkGroupId, String postId, String superCommentId, int offset, int count) throws VKClientException {
       VKResponse<Paginator<VkSubComment>> response = client.getSubComments("-" + vkGroupId, postId, superCommentId,
                                                     offset, count, token);
        if(response.isError()) {
            throw new VKClientException(response.getError());
        }
        return response.getResponse();

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
