package com.thinkhumble.real_time_stock_quote.controller;

import com.thinkhumble.real_time_stock_quote.model.StockQuote;
import com.thinkhumble.real_time_stock_quote.service.AlphaVantageStockQuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Stock Controller", description = "Endpoints for fetching real-time stock quotes")
@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private AlphaVantageStockQuoteService alphaVantageStockQuoteService;

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Operation(summary = "Get a single stock quote by symbol", description = "Returns the latest market quote for a given stock symbol (e.g., AAPL, TSLA).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Stock Quote fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StockQuote.class)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid symbol provided",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(example = "Stock API Error: Error fetching stock quote for symbol AALP. Failed to parse stock quote - No data found for the provided symbol.")
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(example = "Internal server error")
                    ))
    })
    @GetMapping("/quote/{symbol}")
    public ResponseEntity<StockQuote> getQuoteBySymbol(
            @Parameter(
                    description = "The stock symbol to fetch the quote for (e.g., AAPL, TSLA)",
                    required = true,
                    example = "AAPL"
            )
            @PathVariable String symbol
    ) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol must not be null or empty.");
        }

        logger.info("API call: /quote/{}", symbol);
        StockQuote stockQuote = alphaVantageStockQuoteService.getQuoteBySymbol(symbol);
        logger.debug("API response for {}: {}", symbol, stockQuote);
        return ResponseEntity.ok(stockQuote);
    }

    @Operation(summary = "Get batch quotes for multiple symbols", description = "Returns a list of market quotes for multiple stock symbols.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Batch stock quotes fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "array",
                                    implementation = StockQuote.class
                            ),
                            examples = @ExampleObject(
                                    name = "BatchQuotesExample",
                                    description = "A list of stock quotes",
                                    value = """
                                    [
                                      {
                                        "symbol": "AAPL",
                                        "open": 175.0,
                                        "high": 178.5,
                                        "low": 174.2,
                                        "price": 177.1,
                                        "volume": 1000000,
                                        "latestTradingDay": "2025-07-02",
                                        "previousClose": 175.8,
                                        "change": 1.3,
                                        "percentChange": "0.74%"
                                      },
                                      {
                                        "symbol": "TSLA",
                                        "open": 250.0,
                                        "high": 255.0,
                                        "low": 248.5,
                                        "price": 254.1,
                                        "volume": 500000,
                                        "latestTradingDay": "2025-07-02",
                                        "previousClose": 250.8,
                                        "change": 3.3,
                                        "percentChange": "1.32%"
                                      }
                                    ]
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid or empty symbol list provided",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(example = "IllegalArgumentException: Symbols list must not be empty.")
                    )

            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(example = "Internal server error")
                    ))
    })
    @GetMapping("/quotes")
    public ResponseEntity<List<StockQuote>> getBatchQuotes(
            @Parameter(
                    description = "Comma-separated list of stock symbols (e.g., AAPL, TSLA, MSFT)",
                    required = true,
                    example = "AAPL,TSLA,MSFT"
            )
            @RequestParam List<String> symbols
    ) {
        logger.info("API call: /quotes with symbols: {}", symbols);

        if (symbols == null || symbols.isEmpty()) {
            throw new IllegalArgumentException("Symbols list must not be empty.");
        }

        // Filter out empty or blank symbols
        List<String> validSymbols = symbols.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .toList();

        if (validSymbols.isEmpty()) {
            throw new IllegalArgumentException("All symbols are invalid or empty.");
        }

        List<StockQuote> quotes = alphaVantageStockQuoteService.getBatchQuotesBySymbols(symbols);
        logger.debug("API response size: {}", quotes.size());
        return ResponseEntity.ok(quotes);
    }

}
