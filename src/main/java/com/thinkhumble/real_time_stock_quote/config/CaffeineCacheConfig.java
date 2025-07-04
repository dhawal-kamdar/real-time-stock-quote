package com.thinkhumble.real_time_stock_quote.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
//                .expireAfterWrite(1, TimeUnit.MINUTES) // cache validity
                .expireAfterWrite(Duration.ofDays(1)) // Caching for 24 hours - External Api Limit hit prevention
                .maximumSize(100); // max 100 entries
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager manager = new CaffeineCacheManager("quotes");
        manager.setCaffeine(caffeine);
        return manager;
    }
}

