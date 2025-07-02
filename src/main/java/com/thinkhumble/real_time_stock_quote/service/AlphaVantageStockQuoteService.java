package com.thinkhumble.real_time_stock_quote.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkhumble.real_time_stock_quote.config.StockApiConfig;
import com.thinkhumble.real_time_stock_quote.model.StockQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlphaVantageStockQuoteService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private StockApiConfig stockApiConfig;

    private static final Logger logger = LoggerFactory.getLogger(AlphaVantageStockQuoteService.class);

    private StockQuote mapToStockQuote(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode quote = root.path("Global Quote");

            StockQuote stockQuote = new StockQuote();
            stockQuote.setSymbol(quote.path("01. symbol").asText());
            stockQuote.setOpen(quote.path("02. open").asDouble());
            stockQuote.setHigh(quote.path("03. high").asDouble());
            stockQuote.setLow(quote.path("04. low").asDouble());
            stockQuote.setPrice(quote.path("05. price").asDouble());
            stockQuote.setVolume(quote.path("06. volume").asInt());
            stockQuote.setLatestTradingDay(quote.path("07. latest trading day").asText());
            stockQuote.setPreviousClose(quote.path("08. previous close").asDouble());
            stockQuote.setChange(quote.path("09. change").asDouble());
            stockQuote.setPercentChange(quote.path("10. change percent").asText());

            return stockQuote;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse stock quote", e);
        }
    }


    public StockQuote getQuoteBySymbol(String symbol) {
        try {
            logger.info("Fetching stock quote for symbol: {}", symbol);

            String uri = UriComponentsBuilder.fromHttpUrl(stockApiConfig.getUrl())
                    .queryParam("function", stockApiConfig.getFunction())
                    .queryParam("symbol", symbol)
                    .queryParam("apikey", stockApiConfig.getKey())
                    .toUriString();

            logger.debug("API URI: {}", uri);

            StockQuote stockQuote = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(this::mapToStockQuote)
                    .block();

            logger.info("Fetched stock quote for symbol: {} = {}", symbol, stockQuote);
            return stockQuote;
        } catch (Exception e) {
            logger.error("Error occurred while fetching stock quote for symbol {}: {}", symbol, e.getMessage());
            throw new RuntimeException("Error occurred while fetching stock quote for symbol " + symbol, e);
        }
    }

    public List<StockQuote> getBatchQuotesBySymbols(List<String> symbols) {
        try {
            logger.info("Fetching batch stock quote for symbols: {}", symbols);

            List<StockQuote> stockQuoteList = new ArrayList<>();

            for (String symbol : symbols) {
                try {
                    StockQuote quote = getQuoteBySymbol(symbol);
                    if (quote != null) {
                        stockQuoteList.add(quote);
                        logger.debug("Fetched stock quote for symbol: {}", symbol);
                    }
                } catch (Exception e) {
                    logger.warn("Error fetching quote for symbol:  '{}' due to error: {}", symbol, e.getMessage());
                    System.err.println("Error fetching quote for symbol: " + symbol + " due to error: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            logger.info("Fetched batch stock quotes for symbols: {}", stockQuoteList.size());
            return stockQuoteList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
