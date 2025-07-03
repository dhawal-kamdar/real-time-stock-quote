package com.thinkhumble.real_time_stock_quote.controller;

import com.thinkhumble.real_time_stock_quote.model.StockQuote;
import com.thinkhumble.real_time_stock_quote.service.AlphaVantageStockQuoteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockController.class)
class StockControllerTest {

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlphaVantageStockQuoteService stockQuoteService;

    @Test
    void getQuoteBySymbol_shouldReturnStockQuote() throws Exception {
        StockQuote mockQuote = new StockQuote("AAPL", 100.0, 105.0, 98.0, 102.5, 100000, "2025-07-03", 101.0, 1.5, "1.48%");

        when(stockQuoteService.getQuoteBySymbol("AAPL")).thenReturn(mockQuote);

        mockMvc.perform(get("/api/quote/AAPL").with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("AAPL"))
                .andExpect(jsonPath("$.price").value(102.5));
    }

    @Test
    void getQuoteBySymbol_withEmptySymbol_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/quote/ ").with(httpBasic(username, password)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("IllegalArgumentException: Symbol must not be null or empty."));
    }

    @Test
    void getBatchQuotes_shouldReturnQuotes() throws Exception {
        List<StockQuote> mockQuotes = List.of(
                new StockQuote("AAPL", 100.0, 105.0, 98.0, 102.5, 100000, "2025-07-03", 101.0, 1.5, "1.48%"),
                new StockQuote("TSLA", 200.0, 210.0, 198.0, 205.0, 50000, "2025-07-03", 202.0, 3.0, "1.49%")
        );

        when(stockQuoteService.getBatchQuotesBySymbols(anyList())).thenReturn(mockQuotes);

        mockMvc.perform(get("/api/quotes?symbols=AAPL,TSLA").with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].symbol").value("AAPL"))
                .andExpect(jsonPath("$[1].symbol").value("TSLA"));
    }

    @Test
    void getBatchQuotes_withEmptySymbols_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/quotes?symbols= ").with(httpBasic(username, password)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("IllegalArgumentException: All symbols are invalid or empty."));
    }
}