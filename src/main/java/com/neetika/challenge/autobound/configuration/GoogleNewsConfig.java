package com.neetika.challenge.autobound.configuration;

import com.neetika.challenge.autobound.service.GoogleNewsFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class GoogleNewsConfig {

    @Autowired
    private Environment env;

    @Bean
    public GoogleNewsFetcherService googleNewsFetcherService (){
        String newsAPIKey = env.getProperty("newsapi.key");
        String newsAPIHost = env.getProperty("newsapi.host");
        return new GoogleNewsFetcherService( newsAPIKey,newsAPIHost);
    }

}
