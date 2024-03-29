package com.code.mintyn.persistence.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ReactiveCacheManager {
    private final Cache<String, Object> caffeineCache;

    public ReactiveCacheManager() {
        this.caffeineCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .build();
    }

    public <T> Mono<T> get(String key, Class<T> type) {
        T cachedValue = type.cast(caffeineCache.getIfPresent(key));
        if (cachedValue != null) {
            log.info("Cache hit for key: {}", key);
        } else {
            log.info("Cache miss for key: {}", key);
        }
        return Mono.justOrEmpty(cachedValue);
    }

    public void put(String key, Object value) {
        caffeineCache.put(key, value);
        log.info("Cached value for key: {}", key);
    }

    public Map<String, Object> getAllCacheEntries() {
        return caffeineCache.asMap();
    }


}
