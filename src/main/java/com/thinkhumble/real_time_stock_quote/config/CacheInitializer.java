package com.thinkhumble.real_time_stock_quote.config;

import jakarta.annotation.PostConstruct;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheInitializer {

    private final CacheManager cacheManager;

    public CacheInitializer(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @PostConstruct
    public void clearCache() {
        // Disabled cache clearing on startup for better performance, data retention and preventing hitting external API Limit
        // cacheManager.getCache("quotes").clear();
    }
}