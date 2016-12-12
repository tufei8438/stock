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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockInfo {

    private static Logger logger = LoggerFactory.getLogger(StockInfo.class);

    private int ranking;
    private String code;
    private String name;
    private float price;
    private float priceChangePercent;
    private float priceChange;
    private float startPrice;
    private float maxPrice;
    private float minPrice;
    private int volume;
    private int money;

    public void parseFromTrTag(Element tr) {
        Element th = tr.select("th").first();
        if (th != null) {
            this.ranking = Integer.valueOf(th.text());
        }

        Elements tdList = tr.select("td");
        if (tdList.isEmpty() || tdList.size() != 11) {
            return;
        }

        this.code = tdList.get(0).text();
        this.name = tdList.get(1).text();
        this.parseExtFromTdTag(tdList.get(2));
        this.price = getFloat(tdList.get(3));
        this.priceChangePercent = getPercent(tdList.get(4));
        this.priceChange = getFloat(tdList.get(5));
        this.startPrice = getFloat(tdList.get(6));
        this.maxPrice = getFloat(tdList.get(7));
        this.minPrice = getFloat(tdList.get(8));
        this.volume = getInteger(tdList.get(9));
        this.money = getInteger(tdList.get(10));
    }

    protected void parseExtFromTdTag(Element td) {

    }

    protected float getFloat(Element element) {
        String text = element.text();
        try {
            return Float.valueOf(text);
        } catch (NumberFormatException e) {
            logger.warn(String.format("格式化文本:%s出错,不是一个有效的浮点类型", text), e);
            return 0.0f;
        }
    }

    protected float getPercent(Element element) {
        String text = element.text();
        if (text.endsWith("%")) {
            String nText = text.replace("%", "");
            try {
                return Float.valueOf(nText);
            } catch (NumberFormatException e) {
                logger.warn(String.format("格式化文本:%s出错,不是一个有效的百分比数据类型", text), e);
                return 0.0f;
            }
        } else {
            logger.warn(String.format("格式化文本:%s出错,不是一个有效的百分比数据类型", text));
            return 0.0f;
        }
    }

    protected int getInteger(Element element) {
        String text = element.text();
        if (StringUtils.isNumeric(text)) {
            return Integer.valueOf(text);
        } else {
            logger.warn(String.format("格式化文本:%s出错,不是一个有效的整数类型", text));
            return 0;
        }
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(float priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public float getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(float priceChange) {
        this.priceChange = priceChange;
    }

    public float getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(float startPrice) {
        this.startPrice = startPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
