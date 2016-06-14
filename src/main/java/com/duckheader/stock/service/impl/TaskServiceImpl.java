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
package com.duckheader.stock.service.impl;

import com.duckheader.stock.persistence.entity.Trade;
import com.duckheader.stock.service.StockService;
import com.duckheader.stock.service.TaskService;
import com.duckheader.stock.spider.StockTradeSpider;
import com.duckheader.stock.util.ScheduleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    StockService stockService;

    @Override
    public void scheduleStockTradeTask() {
        String stockCode = "sz002177";
        TimerTask task = new GetStockTradeTask(stockCode);
        ScheduleManager scheduleManager = new ScheduleManager(18, ScheduleManager.POLICY_BY_DAY, task);
        scheduleManager.schedule();
    }

    private class GetStockTradeTask extends TimerTask {

        private String stockCode;

        public GetStockTradeTask(String stockCode) {
            this.stockCode = stockCode;
        }

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
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
}
