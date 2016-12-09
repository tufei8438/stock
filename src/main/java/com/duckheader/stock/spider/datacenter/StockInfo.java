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

import org.jsoup.nodes.Element;

public class StockInfo {
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
            System.out.println(th.toString());
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
