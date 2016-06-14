/**
 * Copyright (c) 2016 Smallpay Co. Ltd.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.duckheader.stock.thread;

import com.duckheader.stock.persistence.entity.Company;
import com.duckheader.stock.persistence.entity.Trade;
import com.duckheader.stock.service.StockService;
import com.duckheader.stock.spider.StockTradeSpider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StockTradeRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(StockTradeRunnable.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private String stockCode;

    private StockService stockService;

    private Date startDate;
    private Date endDate;

    public StockTradeRunnable(String stockCode, StockService stockService) {
        this(stockCode, stockService, null, null);
    }

    public StockTradeRunnable(String stockCode, StockService stockService, Date startDate, Date endDate) {
        this.stockCode = stockCode;
        this.stockService = stockService;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void run() {
        Company company = stockService.getCompany(stockCode);
        if (company == null) {
            logger.warn("Can not find company by stock code:" + stockCode);
            return;
        }

        try {
            Calendar spiderCal = Calendar.getInstance();
            if (startDate != null) {
                spiderCal.setTime(startDate);
            }

            if (endDate == null) {
                endDate = company.getListingDate();
            }

            do {
                getStockTradeAndSave(spiderCal);
                Thread.sleep(1000);
                spiderCal.add(Calendar.DAY_OF_MONTH, -1);
            } while (spiderCal.getTime().after(endDate));
        } catch (Exception e) {
            logger.error("StockTradeRunnable has error:" + e.getMessage(), e);
        }
    }

    private void getStockTradeAndSave(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Begin get stock:[" + stockCode + "] trade list. trade date:"
                    + DATE_FORMAT.format(calendar.getTime()));
        }
        StockTradeSpider spider = new StockTradeSpider(stockCode, calendar.getTime());
        List<Trade> tradeList = spider.getTradeList();
        if (!CollectionUtils.isEmpty(tradeList)) {
            stockService.saveTradeList(tradeList);
            if (logger.isDebugEnabled()) {
                logger.debug("Save stock:[" + stockCode + "] trade list size: " + tradeList.size() + " success.");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Stock:[" + stockCode + "] has no trade list. may be is not market day. ");
            }
        }
    }
}
