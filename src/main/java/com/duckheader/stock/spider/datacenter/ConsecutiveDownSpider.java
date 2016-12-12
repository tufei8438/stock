/**
 * Copyright (c) 2016 Smallpay Co. Ltd.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.duckheader.stock.spider.datacenter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConsecutiveDownSpider extends DataCenterSpider {

    @Override
    protected List<StockInfo> fetchByDocument(Document document) {
        List<StockInfo> stocks = new ArrayList<>();
        Element element = document.select("div#ggsj_3_ctn").first();
        if (element == null) {
            return stocks;
        }

        for (Element tr : element.select("tr.alC")) {
            ConsecutiveStockInfo stockInfo = new ConsecutiveStockInfo();
            stockInfo.parseFromTrTag(tr);
            stocks.add(stockInfo);
        }

        return stocks;
    }

    @Override
    protected int getSpiderCategory() {
        // 连续下跌
        return 3;
    }
}
