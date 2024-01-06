package com.code.mintyn.service.abstracts;

import com.code.mintyn.models.CardStatsResponse;
import com.code.mintyn.models.VerifySchemeModel;
import com.code.mintyn.models.dto.VerifyCardDTO;
import com.code.mintyn.persistence.entity.User;
import reactor.core.publisher.Mono;

public interface CardService {
    Mono<VerifySchemeModel> verifyCard(VerifyCardDTO cardDTO);

    CardStatsResponse getCardStats(int start, int limit);
}
