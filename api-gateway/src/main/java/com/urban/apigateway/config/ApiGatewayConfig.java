package com.urban.apigateway.config;

import com.urban.apigateway.filter.ErrorFilter;
import com.urban.apigateway.filter.PostFilter;
import com.urban.apigateway.filter.PreFilter;
import com.urban.apigateway.filter.RouteFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

  @Bean
  public PreFilter preFilter() {
    return new PreFilter();
  }
  @Bean
  public PostFilter postFilter() {
    return new PostFilter();
  }
  @Bean
  public ErrorFilter errorFilter() {
    return new ErrorFilter();
  }
  @Bean
  public RouteFilter routeFilter() {
    return new RouteFilter();
  }
}
