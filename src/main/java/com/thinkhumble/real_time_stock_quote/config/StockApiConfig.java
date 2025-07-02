package com.thinkhumble.real_time_stock_quote.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "stock.api")
public class StockApiConfig {

    private String url;
    private String key;
    private String function;

}
