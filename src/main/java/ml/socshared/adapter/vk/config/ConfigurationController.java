package ml.socshared.adapter.vk.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ml.socshared.adapter.vk.vkclient.VKClient;
import ml.socshared.adapter.vk.vkclient.config.VKJsonResponseUsersListConverter;
import ml.socshared.adapter.vk.vkclient.domain.User;
import ml.socshared.adapter.vk.vkclient.domain.VKResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class ConfigurationController implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<VKResponse<List<User>>>(){}.getType(),
                        new VKJsonResponseUsersListConverter())
                .create();
        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(gson);
        converters.add(gsonConverter);
        //configureMessageConverters(converters);
    }

    @Bean
    VKClient getVkClient() {
        return new VKClient();
    }




}
