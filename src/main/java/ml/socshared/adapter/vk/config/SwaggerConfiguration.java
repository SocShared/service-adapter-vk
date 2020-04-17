package ml.socshared.adapter.vk.config;

import com.google.common.base.Predicate;
import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        Predicate<RequestHandler> controllers =
                RequestHandlerSelectors.basePackage("ml.socshared.adapter.vk.controller");

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("templateSwagger")
                .select()
                .apis(controllers)
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(securitySchemes())
                .forCodeGeneration(true);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Vkontakte Adapter API")
                .version("API 1.0.0")
                .build();
    }

    private List<? extends SecurityScheme> securitySchemes() {
        SecurityScheme scheme = new ApiKey("Bearer", HttpHeaders.AUTHORIZATION, In.HEADER.name());
        return List.of(scheme);
    }
}

