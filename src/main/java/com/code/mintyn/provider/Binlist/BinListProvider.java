package com.code.mintyn.provider.Binlist;

import com.code.mintyn.exception.CardNotFoundException;
import com.code.mintyn.exception.MintynException;
import com.code.mintyn.exception.TooManyRequestsException;
import com.code.mintyn.models.VerifySchemeModel;
import com.code.mintyn.service.AbstractCardProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BinListProvider extends AbstractCardProcessor {

    private final WebClient webClient;

    public BinListProvider(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://lookup.binlist.net/").build();
    }

    @Override
    public String getName() {
        return "binlist";
    }

    @Override
    public Mono<VerifySchemeModel> verifyScheme(String bin) {
        return verifySchemeCall(bin)
                .map(cardResponse -> {
                    return transformToModel(cardResponse);
                })
                .onErrorMap(ex -> new MintynException("Error in BinListProvider", ex));
    }

    private VerifySchemeModel transformToModel(CardResponse cardResponse) {
        return VerifySchemeModel.builder()
                .scheme(cardResponse.getScheme())
                .bank(cardResponse.getBank().getName())
                .type(cardResponse.getType())
                .build();
    }


    public Mono<CardResponse> verifySchemeCall(String bin) {
        return this.webClient.get()
                .uri(bin)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    if (response.statusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
                        return Mono.error(new TooManyRequestsException("Too many requests made to the Binlist API. Please try again later."));
                    }
                    // Handle other 4xx errors, such as 404 Not Found
                    return Mono.error(new CardNotFoundException("Card scheme not found for BIN: " + bin));
                })
                .bodyToMono(CardResponse.class)
                .handle((cardResponse, sink) -> {
                    if (cardResponse == null || isInvalidCardResponse(cardResponse)) {
                        sink.error(new CardNotFoundException("Card scheme not found for BIN: " + bin));
                    } else {
                        sink.next(cardResponse);
                    }
                });
    }


    private boolean isInvalidCardResponse(CardResponse cardResponse) {
        return cardResponse.getScheme() == null || cardResponse.getBank() == null || cardResponse.getType() == null;
    }
}
