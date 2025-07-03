package com.thinkhumble.real_time_stock_quote.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkhumble.real_time_stock_quote.config.StockApiConfig;
import com.thinkhumble.real_time_stock_quote.exception.StockQuoteException;
import com.thinkhumble.real_time_stock_quote.model.StockQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
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

            if (root.has("Error Message")) {
                throw new StockQuoteException("Failed to parse stock quote - Stock API Error: " + root.get("Error Message").asText());
            }

            if (root.has("Information")) {
                throw new StockQuoteException("Stock API Information: " + root.get("Information").asText());
            }

            if (quote.isMissingNode() || quote.size() == 0 || quote.path("01. symbol").asText().isEmpty()) {
                throw new StockQuoteException("Failed to parse stock quote - No data found for the provided symbol.");
            }

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
        } catch (StockQuoteException e) {
            throw e;
        } catch (Exception e) {
            throw new StockQuoteException("Failed to parse stock quote", e);
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

            String response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .map(body -> new StockQuoteException("API error: " + body))
                    )
                    .bodyToMono(String.class)
                    .block();

            logger.info("API Response: " + response);

            return mapToStockQuote(response);
        } catch (Exception e) {
            logger.error("Error fetching stock quote for symbol {} - {}", symbol, e.getMessage());
            throw new StockQuoteException("Error fetching stock quote for symbol " + symbol + " " + e.getMessage());
        }
    }

    public List<StockQuote> getBatchQuotesBySymbols(List<String> symbols) {
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
                logger.error("Error fetching quote for symbol:  '{}' due to error: {}", symbol, e.getMessage());
            }
        }

        logger.info("Fetched batch stock quotes for symbols: {}", stockQuoteList.size());
        return stockQuoteList;
    }


}
