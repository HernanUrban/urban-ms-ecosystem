package com.urban.userservice.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
@EnableWebSecurity
@EnableAutoConfiguration
public class SwaggerConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/**", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/api-docs/**");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.urban.userservice.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(apiKey()));

    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "User Service API",
                "API documentation.",
                "1.0",
                "https://opensource.org/ToS",
                "Hernan Urban",
                "License of API",
                "https://opensource.org/licenses/CDDL-1.0");
        return apiInfo;
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }

}
