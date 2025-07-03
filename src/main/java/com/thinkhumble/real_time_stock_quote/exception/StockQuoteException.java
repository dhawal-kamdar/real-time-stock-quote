package com.thinkhumble.real_time_stock_quote.exception;

public class StockQuoteException extends RuntimeException {
    public StockQuoteException(String message) {
        super(message);
    }

    public StockQuoteException(String message, Throwable e) {
        super(message, e);
    }
}
