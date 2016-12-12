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


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.IOException;

public class StockInfoTest {

    @Test
    public void testParseFromTrTag() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        try {
            HtmlPage htmlPage = webClient.getPage("http://stock.qq.com/data/dctr_modules/ggsj.htm?t=5&change_color_0");
            Document document = Jsoup.parse(htmlPage.asXml());
            Element element = document.select("div#ggsj_5_ctn").first();
            Element tr = element.select("tr.alC").first();
            StockInfo stockInfo = new StockInfo();
            stockInfo.parseFromTrTag(tr);
            System.out.println(stockInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
