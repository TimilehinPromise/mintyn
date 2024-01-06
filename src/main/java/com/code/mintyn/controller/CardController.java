package com.code.mintyn.controller;

import com.code.mintyn.models.ApiResponse;
import com.code.mintyn.models.CardStatsResponse;
import com.code.mintyn.models.VerifySchemeModel;
import com.code.mintyn.models.dto.VerifyCardDTO;
import com.code.mintyn.persistence.cache.ReactiveCacheManager;
import com.code.mintyn.service.abstracts.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/card-scheme", produces = APPLICATION_JSON_VALUE)
public class CardController {

    private final CardService cardService;
    private final ReactiveCacheManager cacheManager;

    public CardController(CardService cardService, ReactiveCacheManager cacheManager) {
        this.cardService = cardService;
        this.cacheManager = cacheManager;
    }


    @GetMapping("/verify/{bin}")
    public Mono<ApiResponse<VerifySchemeModel>> verifyCardScheme(@PathVariable String bin) {
        VerifyCardDTO cardDTO = VerifyCardDTO.builder()
                .bin(bin).build();
        return cardService.verifyCard(cardDTO)
                .map(model -> new ApiResponse<>(true, model))
                .defaultIfEmpty(new ApiResponse<>(false, null));
    }

    @GetMapping("/stats")
    public CardStatsResponse getStats(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int limit) {
        return cardService.getCardStats(start, limit);
    }

    @GetMapping("/cache")
    public ResponseEntity<?> viewCacheContents() {
        return ResponseEntity.ok(cacheManager.getAllCacheEntries());
    }
}

