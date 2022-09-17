package zatribune.spring.jasperreports.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import zatribune.spring.jasperreports.controllers.NotesController;
import zatribune.spring.jasperreports.controllers.ReportingController;

import java.util.ArrayList;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Bean
    public Docket api() {
        //we can control to expose certain apis
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(metaData())
                .tags(new Tag(ReportingController.class.getSimpleName(), "Reporting APIs.")
                        , new Tag(NotesController.class.getSimpleName(), "Notes about this API implementation as a how-to documentation."));
    }


    private ApiInfo metaData() {
        Contact contact = new Contact("Muhammad Ali", "https://www.linkedin.com/in/zatribune/", "muhammadali40k@gmail.com");
        return new ApiInfo(
                "Jasperreports + Spring Boot example",
                "A Demo project for Creating PDF reports in Spring Boot using Jasperreports.",
                "1.0",
                "",
                contact,
                "MIT license",
                "",
                new ArrayList<>()
        );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
