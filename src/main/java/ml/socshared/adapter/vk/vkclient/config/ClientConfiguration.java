package ml.socshared.adapter.vk.vkclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import feign.codec.Decoder;
import feign.gson.GsonDecoder;
import ml.socshared.adapter.vk.vkclient.config.converter.*;
import ml.socshared.adapter.vk.vkclient.domain.*;

import java.util.List;
import java.util.Map;


public class ClientConfiguration {

//    @Bean
//    public Contract feignContract() {
//        return new feign.Contract.Default();
//    }

    public Decoder getDecoder() {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(new TypeToken<VKResponse<List<User>>>(){}.getType(),
//                        new VKJsonResponseUsersListConverter())
//                .registerTypeAdapter(new TypeToken<VKResponse<Paginator<VkGroup>>>(){}.getType(),
//                        new VKJsonResponsePaginationGroupConverter())
//                .registerTypeAdapter(VkGroup.class,
//                        new VKJsonGroupConverter())
//                .registerTypeAdapter(new TypeToken<VKResponse<VkGroup>>(){}.getType(),
//                        new VKJsonResponseGroupConverter())
//                .registerTypeAdapter(new TypeToken<VKResponse<List<VkGroup>>>(){}.getType(),
//                        new VKJsonResponseListGroupsConverter())
//                .create();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<VKResponse<Paginator<VkGroup>>>(){}.getType(),
                        new VKJsonResponseConverter<Paginator<VkGroup>>(new TypeToken<>(){}))
                .registerTypeAdapter(new TypeToken<Paginator<VkGroup>>(){}.getType(),
                        new VKJsonPaginatorConverter<VkGroup>(new TypeToken<>(){}))
                .registerTypeAdapter(new TypeToken<VKResponse<List<VkGroup>>>(){}.getType(),
                        new VKJsonResponseConverter<List<VkGroup>>(new TypeToken<>(){}))
                .registerTypeAdapter(new TypeToken<List<VkGroup>>(){}.getType(),
                        new VKJsonList<VkGroup>(new TypeToken<>(){}))
                .registerTypeAdapter(VkGroup.class,
                        new VKJsonGroupConverter())

                .registerTypeAdapter(new TypeToken<VKResponse<Paginator<Post>>>(){}.getType(),
                        new VKJsonResponseConverter<Paginator<Post>>(new TypeToken<>(){}))
                .registerTypeAdapter(new TypeToken<Paginator<Post>>(){}.getType(),
                        new VKJsonPaginatorConverter<Post>(new TypeToken<>(){}))

                .registerTypeAdapter(new TypeToken<VKResponse<List<Post>>>(){}.getType(),
                        new VKJsonResponseConverter<List<Post>>(new TypeToken<>(){}))
                .registerTypeAdapter(new TypeToken<List<Post>>(){}.getType(),
                        new VKJsonList<Post>(new TypeToken<>(){}))

                .registerTypeAdapter(new TypeToken<VKResponse<Map<String, String>>>(){}.getType(),
                        new VKJsonResponseConverter<Map<String, String>>(new TypeToken<>(){}))

                .registerTypeAdapter(new TypeToken<VKResponse<String>>(){}.getType(),
                        new VKJsonResponseConverter<String>(new TypeToken<>(){}))

                .registerTypeAdapter(new TypeToken<VKResponse<List<VkComment>>>(){}.getType(),
                        new VKJsonResponseConverter<List<VkComment>>(new TypeToken<>(){}))
                .registerTypeAdapter(new TypeToken<List<VkComment>>(){}.getType(),
                        new VKJsonOneCommentConverter())

                .registerTypeAdapter(new TypeToken<VKResponse<VkComment>>(){}.getType(),
                        new VKJsonResponseConverter<VkComment>(new TypeToken<>(){}))

                .registerTypeAdapter(new TypeToken<VKResponse<Paginator<VkComment>>>(){}.getType(),
                        new VKJsonResponseConverter<Paginator<VkComment>>(new TypeToken<>(){}))
                .registerTypeAdapter(new TypeToken<Paginator<VkComment>>(){}.getType(),
                        new VKJsonPaginatorConverter<VkComment>(new TypeToken<>(){}))

                .registerTypeAdapter(new TypeToken<VKResponse<Paginator<VkSubComment>>>(){}.getType(),
                        new VKJsonResponseConverter<Paginator<VkSubComment>>(new TypeToken<>(){}))
                .registerTypeAdapter(new TypeToken<Paginator<VkSubComment>>(){}.getType(),
                        new VKJsonPaginatorConverter<VkSubComment>(new TypeToken<>(){}))

                .registerTypeAdapter(new TypeToken<VKResponse<List<User>>>(){}.getType(),
                        new VKJsonResponseConverter<List<User>>(new TypeToken<>(){}))
                .registerTypeAdapter(new TypeToken<List<User>>(){}.getType(),
                        new VKJsonList<User>(new TypeToken<>(){}))
                .create();
        return new GsonDecoder(gson);
    }

//    @Bean
//    public Encoder getEncoder() {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(new TypeToken<VKResponse<List<User>>>(){}.getType(),
//                        new VKJsonResponseUsersListConverter())
//                .registerTypeAdapter(new TypeToken<VKResponse<Paginator<VkGroup>>>(){}.getType(),
//                        new VKJsonResponsePaginationGroupConverter())
//                .registerTypeAdapter(VkGroup.class,
//                        new VKJsonGroupConverter())
//                .registerTypeAdapter(new TypeToken<VKResponse<VkGroup>>(){}.getType(),
//                        new VKJsonResponseGroupConverter())
//                .create();
//        return new GsonEncoder(gson);
//    }
}
