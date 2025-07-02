package com.thinkhumble.real_time_stock_quote.controller;

import com.thinkhumble.real_time_stock_quote.model.StockQuote;
import com.thinkhumble.real_time_stock_quote.service.AlphaVantageStockQuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private AlphaVantageStockQuoteService alphaVantageStockQuoteService;

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @GetMapping("/quote/{symbol}")
    public ResponseEntity<StockQuote> getQuoteBySymbol(@PathVariable String symbol) {
        logger.info("API call: /quote/{}", symbol);
        StockQuote stockQuote = alphaVantageStockQuoteService.getQuoteBySymbol(symbol);
        logger.debug("API response for {}: {}", symbol, stockQuote);
        return ResponseEntity.ok(stockQuote);
    }

    @GetMapping("/quotes")
    public ResponseEntity<List<StockQuote>> getBatchQuotes(@RequestParam List<String> symbols) {
        logger.info("API call: /quotes with symbols: {}", symbols);
        List<StockQuote> quotes = alphaVantageStockQuoteService.getBatchQuotesBySymbols(symbols);
        logger.debug("API response size: {}", quotes.size());
        return ResponseEntity.ok(quotes);
    }

}
