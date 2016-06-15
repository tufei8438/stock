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

import com.duckheader.stock.persistence.CompanyRepository;
import com.duckheader.stock.persistence.TradeRepository;
import com.duckheader.stock.persistence.entity.Company;
import com.duckheader.stock.persistence.entity.Trade;
import com.duckheader.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.ArrayList;
import java.util.List;

public class StockServiceImpl implements StockService {

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    CompanyRepository companyRepository;

    public void saveTrade(Trade trade) {
        tradeRepository.save(trade);
    }

    @Override
    public void saveTradeList(List<Trade> trades) {
        tradeRepository.save(trades);
    }

    @Override
    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public Company getCompany(String stockCode) {
        return companyRepository.findByStockCode(stockCode);
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        Iterable<Company> companyIterable = companyRepository.findAll();
        for (Company company : companyIterable) {
            companies.add(company);
        }

        return companies;
    }
}
