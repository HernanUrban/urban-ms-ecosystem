package com.urban.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration
    .EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration
    .ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers
    .ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {

  @Value("${auth.server.url}")
  private String authServerUrl;

  @Value("${auth.server.uri.check}")
  private String checkTokenUri;

  @Value("${auth.server.clientId}")
  private String clientId;

  @Value("${auth.server.clientsecret}")
  private String clientSecret;

  @Value("${auth.server.resourceId}")
  private String resourceId;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/health","/info").permitAll()
        .antMatchers(HttpMethod.GET, "/v2/api-docs").permitAll()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        .antMatchers("/users/me").hasAnyRole("USER", "ADMIN")
        .antMatchers(HttpMethod.POST,"/users").permitAll()
        .antMatchers("/users/verify").permitAll()
        .anyRequest()
        .authenticated();
  }

  @Bean
  public AccessTokenConverter accessTokenConverter() {
    return new DefaultAccessTokenConverter();
  }

  @Bean
  public RemoteTokenServices remoteTokenServices() {
    final RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
    remoteTokenServices.setCheckTokenEndpointUrl(authServerUrl + checkTokenUri);
    remoteTokenServices.setClientId(clientId);
    remoteTokenServices.setClientSecret(clientSecret);
    remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
    return remoteTokenServices;
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId(resourceId);
    resources.tokenServices(remoteTokenServices());
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}