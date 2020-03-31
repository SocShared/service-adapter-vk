package ml.socshared.adapter.vk.vkclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import ml.socshared.adapter.vk.vkclient.domain.VkGroup;
import ml.socshared.adapter.vk.vkclient.domain.Paginator;
import ml.socshared.adapter.vk.vkclient.domain.User;
import ml.socshared.adapter.vk.vkclient.domain.VKResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class ClientConfiguration {

//    @Bean
//    public Contract feignContract() {
//        return new feign.Contract.Default();
//    }
    @Bean
    public Decoder getDecoder() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<VKResponse<List<User>>>(){}.getType(),
                        new VKJsonResponseUsersListConverter())
                .registerTypeAdapter(new TypeToken<VKResponse<Paginator<VkGroup>>>(){}.getType(),
                        new VKJsonResponsePaginationGroupConverter())
                .registerTypeAdapter(VkGroup.class,
                        new VKJsonGroupConverter())
                .registerTypeAdapter(new TypeToken<VKResponse<VkGroup>>(){}.getType(),
                        new VKJsonResponseGroupConverter())
                .registerTypeAdapter(new TypeToken<VKResponse<List<VkGroup>>>(){}.getType(),
                        new VKJsonResponseListGroupsConverter())
                .create();
        return new GsonDecoder(gson);
    }

    @Bean
    public Encoder getEncoder() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<VKResponse<List<User>>>(){}.getType(),
                        new VKJsonResponseUsersListConverter())
                .registerTypeAdapter(new TypeToken<VKResponse<Paginator<VkGroup>>>(){}.getType(),
                        new VKJsonResponsePaginationGroupConverter())
                .registerTypeAdapter(VkGroup.class,
                        new VKJsonGroupConverter())
                .registerTypeAdapter(new TypeToken<VKResponse<VkGroup>>(){}.getType(),
                        new VKJsonResponseGroupConverter())
                .create();
        return new GsonEncoder(gson);
    }
}
