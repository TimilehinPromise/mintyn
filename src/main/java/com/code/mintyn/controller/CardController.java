package com.code.mintyn.controller;

import com.code.mintyn.config.utils.UserUtils;
import com.code.mintyn.models.ApiResponse;
import com.code.mintyn.models.VerifySchemeModel;
import com.code.mintyn.models.dto.VerifyCardDTO;
import com.code.mintyn.persistence.entity.User;
import com.code.mintyn.service.abstracts.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/card-scheme", produces = APPLICATION_JSON_VALUE)
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    @GetMapping("/verify/{bin}")
    public Mono<ApiResponse<VerifySchemeModel>> verifyCardScheme(@PathVariable String bin) {
        System.out.println(bin);
        VerifyCardDTO cardDTO = VerifyCardDTO.builder()
                .bin(bin).build();
        User user = UserUtils.getLoggedInUser();
        return cardService.verifyCard(cardDTO,user)
                .map(model -> new ApiResponse<>(true, model))
                .defaultIfEmpty(new ApiResponse<>(false, null));
    }
}

