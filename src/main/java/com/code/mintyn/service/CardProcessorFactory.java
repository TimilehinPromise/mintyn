package com.code.mintyn.service;

import com.code.mintyn.exception.MintynMartRuntimeException;
import com.code.mintyn.service.abstracts.CardProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardProcessorFactory {


    private static final String INVALID_PROCESSOR = "processor is not found";

    private final List<CardProcessor> cardProcessors ;



    @Autowired
    public CardProcessorFactory(List<CardProcessor> processors) {
        this.cardProcessors = processors != null ? new ArrayList<>(processors) : Lists.newArrayList();
    }

    public AbstractCardProcessor getProcessor(String providerName) {

        return cardProcessors.stream()
                .filter(p -> p.getName().equalsIgnoreCase(providerName))
                .map(AbstractCardProcessor.class ::cast)
                .findFirst()
                .orElseThrow(() -> new MintynMartRuntimeException(INVALID_PROCESSOR));
    }
}

