package com.code.mintyn.config.bean;

import com.code.mintyn.provider.Binlist.BinListProvider;
import com.code.mintyn.service.abstracts.CardProcessor;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppBean {

//    @Bean
//    public WebClient webClient(WebClient.Builder webClientBuilder) {
//        return webClientBuilder.baseUrl("https://binlist.net/").build();
//    }

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
