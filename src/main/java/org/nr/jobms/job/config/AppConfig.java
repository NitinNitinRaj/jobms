package org.nr.jobms.job.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {


    //    @Bean
    @LoadBalanced //makes the request in load balanced way.
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
