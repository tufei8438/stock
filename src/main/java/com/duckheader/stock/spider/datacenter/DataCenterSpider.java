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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class DataCenterSpider {

    private static Logger logger = LoggerFactory.getLogger(DataCenterSpider.class);

    public List<String> fetch() {
        try {
            String html = getWebHtml(getSpiderUrl());
            Document document = Jsoup.parse(html);
            return fetchByDocument(document);
        } catch (IOException e) {
            String msg = String.format("处理URL: %s异常", getSpiderUrl());
            logger.error(msg, e);
            return new ArrayList<>();
        }
    }

    private String getWebHtml(String url) throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage htmlPage = webClient.getPage(getSpiderUrl());
        return htmlPage.asXml();
    }

    protected abstract List<String> fetchByDocument(Document document);

    protected abstract String getSpiderUrl();
}
