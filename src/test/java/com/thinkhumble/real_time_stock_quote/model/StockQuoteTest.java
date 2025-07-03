package com.thinkhumble.real_time_stock_quote.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockQuoteTest {

    @Test
    void testConstructorAndGetters() {
        StockQuote quote = new StockQuote(
                "AAPL", 175.5, 180.0, 174.0, 178.2,
                1000000, "2025-07-02", 174.8, 3.4, "1.95%"
        );

        assertEquals("AAPL", quote.getSymbol());
        assertEquals(175.5, quote.getOpen());
        assertEquals(180.0, quote.getHigh());
        assertEquals(174.0, quote.getLow());
        assertEquals(178.2, quote.getPrice());
        assertEquals(1000000, quote.getVolume());
        assertEquals("2025-07-02", quote.getLatestTradingDay());
        assertEquals(174.8, quote.getPreviousClose());
        assertEquals(3.4, quote.getChange());
        assertEquals("1.95%", quote.getPercentChange());
    }

    @Test
    void testSettersAndGetters() {
        StockQuote quote = new StockQuote();

        quote.setSymbol("TSLA");
        quote.setOpen(250.0);
        quote.setHigh(255.5);
        quote.setLow(248.0);
        quote.setPrice(254.8);
        quote.setVolume(500000);
        quote.setLatestTradingDay("2025-07-03");
        quote.setPreviousClose(250.1);
        quote.setChange(4.7);
        quote.setPercentChange("1.88%");

        assertEquals("TSLA", quote.getSymbol());
        assertEquals(250.0, quote.getOpen());
        assertEquals(255.5, quote.getHigh());
        assertEquals(248.0, quote.getLow());
        assertEquals(254.8, quote.getPrice());
        assertEquals(500000, quote.getVolume());
        assertEquals("2025-07-03", quote.getLatestTradingDay());
        assertEquals(250.1, quote.getPreviousClose());
        assertEquals(4.7, quote.getChange());
        assertEquals("1.88%", quote.getPercentChange());
    }

    @Test
    void testToStringContainsSymbol() {
        StockQuote quote = new StockQuote();
        quote.setSymbol("GOOGL");

        String toStringOutput = quote.toString();
        assertTrue(toStringOutput.contains("GOOGL"));
    }
}