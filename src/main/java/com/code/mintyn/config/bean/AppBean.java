package com.code.mintyn.config.bean;

import com.code.mintyn.provider.Binlist.BinListProvider;
import com.code.mintyn.service.abstracts.CardProcessor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableCaching
public class AppBean {


    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public List<CardProcessor> cardProcessorList() {
        List<CardProcessor> processors = new ArrayList<>();
        processors.add( new BinListProvider(webClientBuilder()));
        return processors;
    }

}
