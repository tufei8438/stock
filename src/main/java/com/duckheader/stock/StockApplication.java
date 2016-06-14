package com.duckheader.stock;

import com.duckheader.stock.service.TaskService;
import com.duckheader.stock.util.ScheduleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StockApplication {

    @Autowired
    TaskService taskService;

	public static void main(String[] args) {
		SpringApplication.run(StockApplication.class, args);
	}

    @Bean
    CommandLineRunner scheduleStockTradeTask() {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                taskService.scheduleStockTradeTask();
            }
        };
    }
}
