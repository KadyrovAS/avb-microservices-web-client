package com.avb.config;

//import com.avb.client.UserClient;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


import com.avb.client.CompanyClient;

import java.util.List;

@Configuration
public class ClientConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    private String getServiceUrl(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances.isEmpty()) {
            throw new IllegalStateException("Service " + serviceName + " not found in Eureka");
        }
        ServiceInstance instance = instances.get(0);
        return instance.getUri().toString();
    }

//    @Bean
//    public WebClient userWebClient() {
//        String baseUrl = getServiceUrl("users") + "/api";
//        return WebClient.builder()
//                .baseUrl(baseUrl)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }

    @Bean
    public WebClient companyWebClient() {
        String baseUrl = getServiceUrl("companies") + "/api";
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

//    @Bean
//    public UserClient userClient(WebClient userWebClient) {
//        return HttpServiceProxyFactory
//                .builderFor(WebClientAdapter.create(userWebClient))
//                .build()
//                .createClient(UserClient.class);
//    }

    @Bean
    public CompanyClient companyClient(WebClient companyWebClient) {
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(companyWebClient))
                .build()
                .createClient(CompanyClient.class);
    }
}