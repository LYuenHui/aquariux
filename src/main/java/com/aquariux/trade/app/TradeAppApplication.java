package com.aquariux.trade.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.aquariux.trade.app", "com.aquariux.trade.app.scheduler"})
public class TradeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeAppApplication.class, args);
	}

}
