package com.thinkhumble.real_time_stock_quote.service;

import com.thinkhumble.real_time_stock_quote.model.StockQuote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class StockQuoteCachingService {

    private AlphaVantageStockQuoteService alphaVantageStockQuoteService;

    @Autowired
    public StockQuoteCachingService(@Lazy AlphaVantageStockQuoteService alphaVantageStockQuoteService) {
        this.alphaVantageStockQuoteService = alphaVantageStockQuoteService;
    }

    @Cacheable("quotes")
    public StockQuote getQuoteBySymbol(String symbol) {
        return alphaVantageStockQuoteService.getQuoteBySymbol(symbol);
    }
}