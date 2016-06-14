package com.duckheader.stock;

import com.duckheader.stock.persistence.CompanyRepository;
import com.duckheader.stock.persistence.TradeRepository;
import com.duckheader.stock.persistence.entity.Company;
import com.duckheader.stock.persistence.entity.Trade;
import com.duckheader.stock.service.StockService;
import com.duckheader.stock.spider.StockCompanySpider;
import com.duckheader.stock.spider.StockTradeSpider;
import com.duckheader.stock.thread.StockTradeRunnable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StockApplication.class)
public class StockApplicationTests {

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    StockService stockService;

	@Test
	public void getTradeAndSave() {
        StockTradeSpider spider = new StockTradeSpider("sh600392", "20160421");
        List<Trade> trades = spider.getTradeList();
        tradeRepository.save(trades);
	}

    @Test
    public void getCompanyAndSave() {
        StockCompanySpider spider = new StockCompanySpider("sz002177");
        Company company = spider.getCompanyInfo();
        companyRepository.save(company);
    }

    @Test
    public void getAllTradeByStockCode() {
        Date endDate = new Date(116, 3, 26);
        Date startDate = new Date(116, 4, 25);
        StockTradeRunnable runnable = new StockTradeRunnable("sz002177", stockService, startDate, endDate);
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
