package com.thinkhumble.real_time_stock_quote.service;

import com.thinkhumble.real_time_stock_quote.config.StockApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FinnhubStockQuoteService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private StockApiConfig stockApiConfig;

    public String getQuoteBySymbol(String symbol) {
        String uri = UriComponentsBuilder.fromHttpUrl(stockApiConfig.getUrl())
                .queryParam("symbol", symbol)
                .queryParam("token", stockApiConfig.getKey())
                .toUriString();

        System.out.println("URL: " + uri);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Use `.block()` for synchronous call
    }

}
