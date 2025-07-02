package com.thinkhumble.real_time_stock_quote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockQuote {

    private String symbol;
    private double open;
    private double high;
    private double low;
    private double price;
    private int volume;
    private String latestTradingDay;
    private double previousClose;
    private double change;
    private String percentChange;

}
