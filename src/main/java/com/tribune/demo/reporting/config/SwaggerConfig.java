package com.tribune.demo.reporting.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * For more information: <a href="https://springdoc.org/#migrating-from-springfox">Migration docs.</a>
 **/
@Configuration
@EnableWebMvc
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .pathsToMatch("/")
                .group("spring-boot-jasperreports")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(metaData());
    }


    private Info metaData() {
        Contact contact = new Contact();
        contact.setName("Muhammad Ali");
        contact.setUrl("https://www.linkedin.com/in/zatribune/");
        contact.email("muhammadali40k@gmail.com");

        Info info =  new Info();
        info.setTitle("Jasperreports + Spring Boot example");
        info.contact(contact);
        info.setDescription("A Demo project for Creating PDF reports in Spring Boot using Jasperreports.");
        info.setVersion("1.0");
        info.setLicense(new License().name("Apache 2.0"));

        return info;
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
}
