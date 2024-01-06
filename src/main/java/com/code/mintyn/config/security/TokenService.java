package com.code.mintyn.config.security;


import com.code.mintyn.persistence.entity.TokenStore;
import com.code.mintyn.persistence.repository.TokenStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.runAsync;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenStoreRepository tokenStoreRepository;

    public CompletableFuture<Void> deleteExpiredTokens() {
        return runAsync(() -> {
            List<TokenStore> expiredTokens = tokenStoreRepository.findTokenStoreByExpiredAtIsLessThanEqual(LocalDateTime.now());
            tokenStoreRepository.deleteAll(expiredTokens);
        });
    }
}
