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
package com.duckheader.stock.spider;

import com.duckheader.stock.persistence.entity.Company;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockCompanySpider {

    private static final Logger logger = LoggerFactory.getLogger(StockCompanySpider.class);

    private static final String GET_COMPANY_URL_BASE = "http://stock.finance.qq.com/corp1/profile.php";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private String stockCode;

    public StockCompanySpider(String stockCode) {
        this.stockCode = stockCode;
    }

    public Company getCompanyInfo() {
        try {
            Document doc = Jsoup.connect(getCompanyUrl()).get();
            return getCompanyFromDocument(doc);
        } catch (IOException e) {
            logger.error("Get Company from:[" + getCompanyUrl() + "] error.", e);
            return null;
        }
    }

    private Company getCompanyFromDocument(Document document) {
        Company company = new Company();
        company.setStockCode(stockCode);
        company.setLegalName(findTableText(document, "法定名称"));
        company.setEnglishName(findTableText(document, "英文名称"));
        String issuePrice = findTableText(document, "发行价格(元)");
        if (issuePrice == null || !StringUtils.isNumeric(issuePrice)) {
            company.setIssuePrice(null);
        } else {
            company.setIssuePrice(Float.valueOf(issuePrice));
        }
        company.setBusinessScope(findTableText(document, "经营范围"));
        company.setLegalPerson(findTableText(document, "法人代表"));
        company.setListingDate(parseDate(findTableText(document, "上市日期")));
        company.setRegisterAddress(findTableText(document, "注册地址"));
        company.setBrief(findTableText(document, "公司简介"));

        return company;
    }

    private Date parseDate(String source) {
        try {
            return DATE_FORMAT.parse(source);
        } catch (ParseException e) {
            logger.error("Parse date text :[" + source + "] error.", e);
            return null;
        }
    }

    private String findTableText(Document document, String key) {
        Element e = document.select(String.format("td:contains(%s)", key)).first();
        if (e == null) {
            return null;
        }

        Element nextElement = e.nextElementSibling();
        if (nextElement == null) {
            return null;
        }

        return nextElement.ownText();
    }

    private String getCompanyUrl() {
        String zqdm;
        if (stockCode.startsWith("sh") || stockCode.startsWith("sz")) {
            zqdm = stockCode.substring(2);
        } else {
            zqdm = stockCode;
        }

        return String.format("%s?zqdm=%s", GET_COMPANY_URL_BASE, zqdm);
    }
}
