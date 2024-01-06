package com.code.mintyn.service.concrete;

import com.code.mintyn.config.AppConfig;
import com.code.mintyn.models.CardStatsResponse;
import com.code.mintyn.models.VerifySchemeModel;
import com.code.mintyn.models.dto.VerifyCardDTO;
import com.code.mintyn.persistence.cache.ReactiveCacheManager;
import com.code.mintyn.persistence.entity.Card;
import com.code.mintyn.persistence.repository.CardRepository;
import com.code.mintyn.provider.Binlist.BinListProvider;
import com.code.mintyn.service.CardProcessorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private AppConfig appConfig;
    @Mock
    private CardProcessorFactory processorFactory;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private ReactiveCacheManager cacheManager;
    @Mock
    private BinListProvider mockProcessor;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card card;
    private String validBin;
    private VerifySchemeModel cachedModel;
    private VerifySchemeModel processorModel;

    @BeforeEach
    void setUp() {
        validBin = "49218184";
        card = new Card(validBin, "visa", "debit", "Lloyds Bank Plc", 3);
        cachedModel = new VerifySchemeModel("visa", "debit", "Test Bank");
        processorModel = new VerifySchemeModel("visa", "debit", "Processor Bank");
    }

    @Test
    void verifyCard_CacheHit_ShouldReturnCachedValue() {
        VerifyCardDTO cardDTO = new VerifyCardDTO(validBin, "");

        when(cacheManager.get(validBin, VerifySchemeModel.class)).thenReturn(Mono.just(cachedModel));
        when(cardRepository.findByBin(validBin)).thenReturn(Optional.empty());

        StepVerifier.create(cardService.verifyCard(cardDTO))
                .expectNext(cachedModel)
                .verifyComplete();

        verify(cardRepository).findByBin(validBin);
        verify(processorFactory, never()).getProcessor(anyString());
    }

    @Test
    void verifyCard_CacheMissDatabaseHit_ShouldFetchFromDatabase() {
        VerifyCardDTO cardDTO = new VerifyCardDTO(validBin, "");

        when(cacheManager.get(validBin, VerifySchemeModel.class)).thenReturn(Mono.empty());
        when(cardRepository.findByBin(validBin)).thenReturn(Optional.of(card));

        cardService.verifyCard(cardDTO).block();

        verify(cardRepository, times(2)).findByBin(validBin); // Once for fetch, once for increment count
        verify(cacheManager).put(eq(validBin), any(VerifySchemeModel.class));
        verify(processorFactory, never()).getProcessor(anyString());
    }

    @Test
    void verifyCard_CacheMissProcessorFetch_ShouldFetchFromProcessor() {
        VerifyCardDTO cardDTO = new VerifyCardDTO(validBin, "");

        when(cacheManager.get(validBin, VerifySchemeModel.class)).thenReturn(Mono.empty());
        when(cardRepository.findByBin(validBin)).thenReturn(Optional.empty());
        when(processorFactory.getProcessor(anyString())).thenReturn(mockProcessor);
        when(mockProcessor.verifyScheme(validBin)).thenReturn(Mono.just(processorModel));

        cardService.verifyCard(cardDTO).block();

        verify(cardRepository, times(2)).findByBin(validBin);
        verify(cacheManager).put(eq(validBin), any(VerifySchemeModel.class));
        verify(processorFactory).getProcessor(anyString());
    }

    @Test
    void getCardStats_ShouldReturnCardStatistics() {
        int start = 0;
        int limit = 10;
        List<Card> cards = List.of(new Card("123456", "visa", "debit", "Bank A", 5),
                new Card("654321", "mastercard", "credit", "Bank B", 3));
        Page<Card> cardPage = new PageImpl<>(cards, PageRequest.of(start, limit), cards.size());

        when(cardRepository.findAll(PageRequest.of(start, limit))).thenReturn(cardPage);

        CardStatsResponse response = cardService.getCardStats(start, limit);

        assertEquals(true, response.isSuccess());
        assertEquals(start, response.getStart());
        assertEquals(limit, response.getLimit());
        assertEquals(cards.size(), response.getSize());
        assertEquals(cards.stream().collect(Collectors.toMap(Card::getBin, Card::getCount)), response.getPayload());
    }

    private VerifySchemeModel convertToVerifySchemeModel(Card card) {
        return VerifySchemeModel.builder()
                .scheme(card.getScheme())
                .bank(card.getBank())
                .type(card.getType())
                .build();
    }
}
