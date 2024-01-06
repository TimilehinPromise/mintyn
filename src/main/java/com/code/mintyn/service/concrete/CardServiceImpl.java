package com.code.mintyn.service.concrete;

import com.code.mintyn.config.AppConfig;
import com.code.mintyn.models.VerifySchemeModel;
import com.code.mintyn.models.dto.VerifyCardDTO;
import com.code.mintyn.persistence.entity.Card;
import com.code.mintyn.persistence.entity.User;
import com.code.mintyn.persistence.repository.CardRepository;
import com.code.mintyn.service.AbstractCardProcessor;
import com.code.mintyn.service.CardProcessorFactory;
import com.code.mintyn.service.abstracts.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {


    private final AppConfig appConfig;
    private final CardProcessorFactory processorFactory;
    private final CardRepository cardRepository;

    @Override
    public Mono<VerifySchemeModel> verifyCard(VerifyCardDTO cardDTO, User user) {
        return Mono.fromCallable(() -> cardRepository.findByBin(cardDTO.getBin()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalCard -> {
                    if (optionalCard.isPresent()) {
                        Card card = optionalCard.get();
                        card.setCount(card.getCount() + 1);
                        cardRepository.save(card);

                        return Mono.just(VerifySchemeModel.builder()
                                .scheme(card.getScheme())
                                .bank(card.getBank())
                                .type(card.getType())
                                .build());
                    } else {
                        if (Objects.isNull(cardDTO.getProvider())) {
                            cardDTO.setProvider(appConfig.getDefaultCardProcessor());
                        }

                        AbstractCardProcessor cardProcessor = processorFactory.getProcessor(cardDTO.getProvider());
                        return cardProcessor.verifyScheme(cardDTO.getBin())
                                .flatMap(verifySchemeModel ->
                                        Mono.fromRunnable(() -> saveToDatabase(verifySchemeModel, user, cardDTO.getBin()))
                                                .subscribeOn(Schedulers.boundedElastic())
                                                .thenReturn(verifySchemeModel)
                                );
                    }
                });
    }



    private void saveToDatabase(VerifySchemeModel verifySchemeModel, User user, String bin) {
        Card card = Card.builder()
                .bin(bin)
                .count(1)
                .type(verifySchemeModel.getType())
                .bank(verifySchemeModel.getBank())
                .scheme(verifySchemeModel.getScheme())
                .user(user)
                .build();

         cardRepository.save(card);
    }




}
