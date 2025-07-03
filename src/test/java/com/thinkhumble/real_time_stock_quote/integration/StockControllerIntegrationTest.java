package com.thinkhumble.real_time_stock_quote.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StockControllerIntegrationTest {

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Autowired
    private MockMvc mockMvc;

    // Skip test if rate limit message is detected
    private void skipIfRateLimitHit(String responseBody) {
        assumeTrue(!responseBody.contains("standard API rate limit"), "Skipped due to API rate limit.");
    }

    @Test
    void testGetQuoteBySymbol_withValidSymbol_returnsQuote() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/quote/AAPL")
                        .with(httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        skipIfRateLimitHit(responseBody);

        int status = result.getResponse().getStatus();
        assertEquals(200, status, "Expected status 200 OK");

        JsonNode json = new ObjectMapper().readTree(responseBody);
        assertEquals("AAPL", json.get("symbol").asText());
        assertNotNull(json.get("price"));
        assertNotNull(json.get("latestTradingDay"));
    }

    @Test
    void testGetQuoteBySymbol_withInvalidSymbol_throwsException() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/quote/INVALIDSYM")
                        .with(httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        skipIfRateLimitHit(responseBody);

        int status = result.getResponse().getStatus();
        assertEquals(400, status);
        assertTrue(responseBody.contains("Failed to parse stock quote"));
    }

    @Test
    void testGetBatchQuotesBySymbols_withMultipleSymbols() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/quotes?symbols=AAPL,TSLA")
                        .with(httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        JsonNode json = new ObjectMapper().readTree(responseBody);

        // If we get empty array, assume rate limit may be the cause
        assumeTrue(json.isArray() && json.size() > 0, "Skipped due to possible API rate limit (empty array received)");

        int status = result.getResponse().getStatus();
        assertEquals(200, status);

        assertEquals(2, json.size());
        assertNotNull(json.get(0).get("symbol"));
        assertNotNull(json.get(1).get("symbol"));
    }

}
