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
package com.duckheader.stock.rest;

import com.duckheader.stock.persistence.entity.Company;
import com.duckheader.stock.service.StockService;
import com.duckheader.stock.spider.StockCompanySpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockResource {

    @Autowired
    StockService stockService;

    @RequestMapping(value = "/api/stock/companies/{stockCode}")
    public Company getCompanyInfo(@PathVariable(value = "stockCode") String stockCode) {
        Company company = stockService.getCompany(stockCode);
        if (company == null) {
            StockCompanySpider spider = new StockCompanySpider(stockCode);
            company = spider.getCompanyInfo();
            if (company != null) {
                stockService.saveCompany(company);
            } else {
                throw new IllegalArgumentException("无效的股票代码");
            }
        }

        return company;
    }
}
