package com.avb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class ClientService{
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final String postfix = "/api";

    @Autowired
    private DiscoveryClient discoveryClient;

    public <T> T getClient(Class<T> clientClass, String serviceName) {
        WebClient companyWebClient = getWebClient(serviceName);
        if (companyWebClient == null) {
            return null;
        }

        logger.info("Client {} was created!", serviceName);
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(companyWebClient))
                .build()
                .createClient(clientClass);
    }


    public WebClient getWebClient(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances.isEmpty()) {
            logger.error("Service {} not found in Eureka", serviceName);
            return null;
        }
        ServiceInstance instance = instances.get(0);
        String url = instance.getUri().toString();
        logger.info("Found service {} at URL: {}", serviceName, url);

        if (url == null) {
            return null;
        }

        return WebClient.builder()
                .baseUrl(url + postfix)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
