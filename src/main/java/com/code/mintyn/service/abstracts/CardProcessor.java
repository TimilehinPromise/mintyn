package com.code.mintyn.service.abstracts;

import com.code.mintyn.models.VerifySchemeModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface CardProcessor {

    String getName();

    Mono<VerifySchemeModel> verifyScheme(String bin);
}
