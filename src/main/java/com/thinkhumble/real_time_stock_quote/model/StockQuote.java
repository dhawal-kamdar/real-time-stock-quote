package com.thinkhumble.real_time_stock_quote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Represents a real-time stock quote with market data.")
public class StockQuote {

    @Schema(description = "Stock symbol (e.g., AAPL, MSFT)", example = "AAPL")
    private String symbol;

    @Schema(description = "Opening price of the stock for the trading day", example = "187.45")
    private double open;

    @Schema(description = "Highest price during the trading day", example = "190.80")
    private double high;

    @Schema(description = "Lowest price during the trading day", example = "186.20")
    private double low;

    @Schema(description = "Current market price", example = "188.75")
    private double price;

    @Schema(description = "Number of shares traded during the trading day", example = "45322103")
    private int volume;

    @Schema(description = "Date of the latest trading day", example = "2025-07-02")
    private String latestTradingDay;

    @Schema(description = "Price at the close of the previous trading day", example = "186.55")
    private double previousClose;

    @Schema(description = "Price change since previous close", example = "2.20")
    private double change;

    @Schema(description = "Percentage change since previous close", example = "1.18%")
    private String percentChange;

    public StockQuote() {}

    public StockQuote(String symbol, double open, double high, double low, double price, int volume, String latestTradingDay, double previousClose, double change, String percentChange) {
        this.symbol = symbol;
        this.open = open;
        this.high = high;
        this.low = low;
        this.price = price;
        this.volume = volume;
        this.latestTradingDay = latestTradingDay;
        this.previousClose = previousClose;
        this.change = change;
        this.percentChange = percentChange;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getLatestTradingDay() {
        return latestTradingDay;
    }

    public void setLatestTradingDay(String latestTradingDay) {
        this.latestTradingDay = latestTradingDay;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    @Override
    public String toString() {
        return "StockQuote{" +
                "symbol='" + symbol + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", price=" + price +
                ", volume=" + volume +
                ", latestTradingDay='" + latestTradingDay + '\'' +
                ", previousClose=" + previousClose +
                ", change=" + change +
                ", percentChange='" + percentChange + '\'' +
                '}';
    }

}
