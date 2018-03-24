package com.urban.urbanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class UrbanServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UrbanServiceApplication.class, args);
  }
}
