package ml.socshared.adapter.vk.vkclient;

import feign.Feign;
import ml.socshared.adapter.vk.vkclient.config.ClientConfiguration;
import ml.socshared.adapter.vk.vkclient.domain.*;
import ml.socshared.adapter.vk.vkclient.exception.InvalidParameterException;
import ml.socshared.adapter.vk.vkclient.exception.VKClientException;

import java.util.List;


public class VKClient {


   public VKClient(String accessToken) {
       ClientConfiguration config = new ClientConfiguration();
        this.token = accessToken;
        this.client = Feign.builder()
                .decoder(config.getDecoder())
                .encoder(config.getEncoder())
                .target(VKFeignClient.class, "https://api.vk.com/method");
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
       StringBuilder fieldsList = converListStringToString(fields);
       VKResponse<List<VkGroup>> response = client.getGroupInfo(groupId, fieldsList.toString(), token);
       if(response.isError() || response.getResponse().size() == 0) {
           throw new VKClientException(response.getError());
       } else {
           return response.getResponse().get(0);
       }
    }

    public Paginator<VkGroup> getGroupsInfo(String userId, List<String> fields,
                                            List<String> filter, int offset, int count) throws VKClientException {
        StringBuilder fieldsList = converListStringToString(fields);
        StringBuilder filtersList = converListStringToString(filter);
       VKResponse<Paginator<VkGroup>> response = client.getGroupsInfo(fieldsList.toString(),
                                                                     1,
                                                                     filtersList.toString(), token,
                                                                     offset, count);
       if(response.isError()) {
           throw new VKClientException(response.getError());
       }
       return response.getResponse();
    }

    public Paginator<Post> getPostsOfGroup(String groupId, String filter, int count, int offset) throws VKClientException {
       VKResponse<Paginator<Post>> response = client.getPosts(groupId, offset, count, filter, token);
       if(response.isError()) {
           throw new VKClientException(response.getError());
       } else {
           return response.getResponse();
       }
    }

    private static StringBuilder converListStringToString(List<String> list) {
       StringBuilder str = new StringBuilder();
       for(int i = 0; i < list.size()-1; i++) {
            str.append(list.get(i));
            str.append(',');
        }
        str.append(list.get(list.size()-1));
       return str;
    }

    private VKFeignClient client;
    private String token;
}
