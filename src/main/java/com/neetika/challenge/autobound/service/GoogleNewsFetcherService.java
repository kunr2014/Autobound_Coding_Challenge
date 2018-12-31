package com.neetika.challenge.autobound.service;

import com.neetika.challenge.autobound.dto.NewsAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class GoogleNewsFetcherService {

    private String apiKey;
    private String apiHost;
    private RestTemplate restTemplate;

    public GoogleNewsFetcherService(String apiKey, String apiHost) {
        this.apiKey = apiKey;
        this.apiHost = apiHost;
        this.restTemplate = new RestTemplate();
    }

    public NewsAPIResponse fetchArticles(String query) {
        log.info("Fetching Articles using query: {}", query);
        String resourceURL = String.format("%s?q=%s&apiKey=%s", apiHost, query, apiKey);
        return this.restTemplate.getForObject(resourceURL, NewsAPIResponse.class);
    }

}
