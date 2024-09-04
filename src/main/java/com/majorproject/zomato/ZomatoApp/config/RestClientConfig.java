package com.majorproject.zomato.ZomatoApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class RestClientConfig {

    private static final String BASE_URL_OSRM_SERVER = "http://router.project-osrm.org/route/v1/driving/";

    @Bean
    RestClient restClient() {

        return RestClient
                .builder()
                .baseUrl(BASE_URL_OSRM_SERVER)
                .defaultHeader(CONTENT_TYPE , APPLICATION_JSON_VALUE)
                .build();
    }
}
