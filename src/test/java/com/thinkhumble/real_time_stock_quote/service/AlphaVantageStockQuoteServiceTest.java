package com.thinkhumble.real_time_stock_quote.service;

import com.thinkhumble.real_time_stock_quote.config.StockApiConfig;
import com.thinkhumble.real_time_stock_quote.exception.StockQuoteException;
import com.thinkhumble.real_time_stock_quote.model.StockQuote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlphaVantageStockQuoteServiceTest {

    @InjectMocks
    private AlphaVantageStockQuoteService stockQuoteService;

    @Mock
    private WebClient webClient;

    @Mock
    private StockApiConfig stockApiConfig;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(stockApiConfig.getUrl()).thenReturn("https://dummy-url.com");
        when(stockApiConfig.getFunction()).thenReturn("GLOBAL_QUOTE");
        when(stockApiConfig.getKey()).thenReturn("dummy-key");
    }

    @Test
    void testGetQuoteBySymbol_success() {
        String symbol = "AAPL";

        String dummyApiResponse = """
            {
                "Global Quote": {
                    "01. symbol": "AAPL",
                    "02. open": "172.00",
                    "03. high": "174.00",
                    "04. low": "171.50",
                    "05. price": "173.50",
                    "06. volume": "100000",
                    "07. latest trading day": "2025-07-03",
                    "08. previous close": "171.90",
                    "09. change": "1.60",
                    "10. change percent": "0.93%"
                }
            }
            """;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(dummyApiResponse));

        StockQuote quote = stockQuoteService.getQuoteBySymbol(symbol);

        assertNotNull(quote);
        assertEquals("AAPL", quote.getSymbol());
        assertEquals(172.00, quote.getOpen());
        assertEquals(173.50, quote.getPrice());
    }

    @Test
    void testGetQuoteBySymbol_invalidSymbol() {
        String invalidResponse = """
            {
                "Global Quote": {}
            }
            """;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(invalidResponse));

        assertThrows(StockQuoteException.class, () -> stockQuoteService.getQuoteBySymbol("XXXX"));
    }

    @Test
    void testGetBatchQuotesBySymbols_mixedSuccessAndFailure() {
        List<String> symbols = Arrays.asList("AAPL", "INVALID");

        // AAPL Success Response
        String aaplResponse = """
            {
                "Global Quote": {
                    "01. symbol": "AAPL",
                    "02. open": "172.00",
                    "03. high": "174.00",
                    "04. low": "171.50",
                    "05. price": "173.50",
                    "06. volume": "100000",
                    "07. latest trading day": "2025-07-03",
                    "08. previous close": "171.90",
                    "09. change": "1.60",
                    "10. change percent": "0.93%"
                }
            }
            """;

        String invalidResponse = """
            {
                "Global Quote": {}
            }
            """;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("AAPL"))).thenReturn(requestHeadersSpec);
        when(requestHeadersUriSpec.uri(contains("INVALID"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.just(aaplResponse))
                .thenReturn(Mono.just(invalidResponse));

        List<StockQuote> results = stockQuoteService.getBatchQuotesBySymbols(symbols);
        assertEquals(1, results.size());
        assertEquals("AAPL", results.get(0).getSymbol());
    }
}
