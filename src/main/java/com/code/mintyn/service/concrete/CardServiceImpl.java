package com.code.mintyn.service.concrete;

import com.code.mintyn.config.AppConfig;
import com.code.mintyn.models.CardStatsResponse;
import com.code.mintyn.models.VerifySchemeModel;
import com.code.mintyn.models.dto.VerifyCardDTO;
import com.code.mintyn.persistence.cache.ReactiveCacheManager;
import com.code.mintyn.persistence.entity.Card;
import com.code.mintyn.persistence.repository.CardRepository;
import com.code.mintyn.service.AbstractCardProcessor;
import com.code.mintyn.service.CardProcessorFactory;
import com.code.mintyn.service.abstracts.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {


    private final AppConfig appConfig;
    private final CardProcessorFactory processorFactory;
    private final CardRepository cardRepository;
    private final ReactiveCacheManager cacheManager;


    @Override
    public Mono<VerifySchemeModel> verifyCard(VerifyCardDTO cardDTO) {
        return cacheManager.get(cardDTO.getBin(), VerifySchemeModel.class)
                .switchIfEmpty(Mono.defer(() -> fetchCardDetails(cardDTO)))
                .doOnSuccess(model -> incrementCardCount(cardDTO.getBin()));
    }

    private Mono<VerifySchemeModel> fetchCardDetails(VerifyCardDTO cardDTO) {
        return Mono.fromCallable(() -> cardRepository.findByBin(cardDTO.getBin()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalCard -> processCard(optionalCard, cardDTO));
    }


    private Mono<VerifySchemeModel> processCard(Optional<Card> optionalCard, VerifyCardDTO cardDTO) {
        if (optionalCard.isPresent()) {
            VerifySchemeModel model = convertToVerifySchemeModel(optionalCard.get());
            cacheManager.put(cardDTO.getBin(), model);
            return Mono.just(model);
        } else {
            return fetchFromProcessor(cardDTO);
        }
    }


    private Mono<VerifySchemeModel> fetchFromProcessor(VerifyCardDTO cardDTO) {
        if (Objects.isNull(cardDTO.getProvider())) {
            cardDTO.setProvider(appConfig.getDefaultCardProcessor());
        }
        AbstractCardProcessor cardProcessor = processorFactory.getProcessor(cardDTO.getProvider());
        return cardProcessor.verifyScheme(cardDTO.getBin())
                .flatMap(verifySchemeModel -> {
                    saveToDatabase(verifySchemeModel, cardDTO.getBin());
                    return Mono.fromRunnable(() -> cacheManager.put(cardDTO.getBin(), verifySchemeModel))
                            .thenReturn(verifySchemeModel);
                });
    }


    private void incrementCardCount(String bin) {
        cardRepository.findByBin(bin)
                .ifPresent(card -> {
                    card.setCount(card.getCount() + 1);
                    cardRepository.save(card);
                });
    }


    @Override
    public CardStatsResponse getCardStats(int start, int limit) {
        Pageable pageable = PageRequest.of(start, limit);
        Page<Card> cardPage = cardRepository.findAll(pageable);
        Map<String, Integer> payload = cardPage.getContent().stream()
                .collect(Collectors.toMap(Card::getBin, Card::getCount));

        return new CardStatsResponse(true, start, limit, cardPage.getTotalElements(), payload);
    }


    private void saveToDatabase(VerifySchemeModel verifySchemeModel, String bin) {
        Card card = Card.builder()
                .bin(bin)
                .count(0)
                .type(verifySchemeModel.getType())
                .bank(verifySchemeModel.getBank())
                .scheme(verifySchemeModel.getScheme())
                .build();

         cardRepository.save(card);
    }

    private VerifySchemeModel convertToVerifySchemeModel(Card card) {
        return VerifySchemeModel.builder()
                .scheme(card.getScheme())
                .bank(card.getBank())
                .type(card.getType())
                .build();
    }




}
