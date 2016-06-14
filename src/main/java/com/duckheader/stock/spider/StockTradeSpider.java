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

import com.duckheader.stock.persistence.entity.Trade;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StockTradeSpider {

    private static final String TRADE_URI_BASE = "http://stock.gtimg.cn/data/index.php";
    private static final SimpleDateFormat QUERY_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    private String stockCode;
    private String queryDate;

    public StockTradeSpider(String stockCode, Date queryDate) {
        this.stockCode = stockCode;
        this.queryDate = QUERY_DATE_FORMAT.format(queryDate);
    }

    public StockTradeSpider(String stockCode, String queryDate) {
        this.stockCode = stockCode;
        this.queryDate = queryDate;
    }

    public List<Trade> getTradeList() {
        List<Trade> trades = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(generateTradeUri());
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                Header contentType = response.getLastHeader("Content-Type");
                if (contentType.getValue().equalsIgnoreCase("application/x-msexcel")) {
                    HttpEntity httpEntity = response.getEntity();
                    if (httpEntity != null) {
                        InputStream content = httpEntity.getContent();
                        trades = readCSV(content);
                    }
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return trades;
    }

    private String generateTradeUri() {
        return String.format("%s?appn=detail&action=download&c=%s&d=%s",
                TRADE_URI_BASE, stockCode, queryDate);
    }

    private List<Trade> readCSV(InputStream inputStream) {
        List<Trade> trades = new ArrayList<>();
        Reader reader = null;
        CSVParser parser = null;
        try {
            reader = new InputStreamReader(new BOMInputStream(inputStream), "GB2312");
            parser = new CSVParser(reader, CSVFormat.TDF.withSkipHeaderRecord());
            for (CSVRecord record : parser) {
                // 忽略掉首行
                if (record.getRecordNumber() > 1) {
                    Trade trade = parseTradeFromRecord(record);
                    trades.add(trade);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }

                if (parser != null) {
                    parser.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return trades;
    }

    private List<Trade> readExcel(InputStream inputStream) {
        List<Trade> trades = new ArrayList<>();
        try {
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.rowIterator();
            while(iterator.hasNext()) {
                Row row = iterator.next();
                if (sheet.getFirstRowNum() != row.getRowNum()) {
                    Trade trade = parseTradeFromRow(row);
                    trades.add(trade);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return trades;
    }

    private Trade parseTradeFromRecord(CSVRecord record) {
        Trade trade = new Trade();
        trade.setStockCode(stockCode);
        trade.setTradeTime(getTradeTime(record.get(0)));
        trade.setTradePrice(Float.valueOf(record.get(1)));
        trade.setPriceChange(Float.valueOf(record.get(2)));
        trade.setVolume(Integer.valueOf(record.get(3)));
        trade.setAmount(Integer.valueOf(record.get(4)));
        String nature = record.get(5);
        trade.setNature(nature);
        return trade;
    }

    private Trade parseTradeFromRow(Row row) {
        Trade trade = new Trade();
        trade.setStockCode(stockCode);
        Date tradeTime = getTradeTime(row.getCell(0).getStringCellValue());
        trade.setTradeTime(tradeTime);
        trade.setTradePrice(Float.valueOf(row.getCell(1).getStringCellValue()));
        trade.setPriceChange(Float.valueOf(row.getCell(2).getStringCellValue()));
        trade.setVolume(Integer.valueOf(row.getCell(3).getStringCellValue()));
        trade.setAmount(Integer.valueOf(row.getCell(4).getStringCellValue()));
        trade.setNature(row.getCell(5).getStringCellValue());
        return trade;
    }

    private Date getTradeTime(String value) {
        String tradeTime;
        if (value.length() == 7) {
            tradeTime = String.format("%s 0%s", queryDate, value);
        } else {
            tradeTime = String.format("%s %s", queryDate, value);
        }
        try {
            return DATE_FORMAT.parse(tradeTime);
        } catch (ParseException e) {
            return null;
        }
    }


}
