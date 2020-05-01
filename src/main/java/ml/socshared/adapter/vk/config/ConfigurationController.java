package ml.socshared.adapter.vk.config;

import ml.socshared.adapter.vk.vkclient.VKClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ConfigurationController implements WebMvcConfigurer {

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(new TypeToken<VKResponse<List<User>>>(){}.getType(),
//                        new VKJsonResponseUsersListConverter())
//                .create();
//        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
//        gsonConverter.setGson(gson);
//        converters.add(gsonConverter);
//        //configureMessageConverters(converters);
//    }

    @Bean
    VKClient getVkClient() {
        return new VKClient();
    }




}
