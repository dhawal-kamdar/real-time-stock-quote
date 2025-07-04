package com.thinkhumble.real_time_stock_quote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RealTimeStockQuoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealTimeStockQuoteApplication.class, args);
	}

}
