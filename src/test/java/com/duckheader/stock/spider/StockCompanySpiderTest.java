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
import org.junit.Assert;
import org.junit.Test;

public class StockCompanySpiderTest {

    @Test
    public void getCompanyInfo() {
        StockCompanySpider spider = new StockCompanySpider("601919");
        Company company = spider.getCompanyInfo();
        Assert.assertNotNull(company);
        Assert.assertEquals("中国远洋控股股份有限公司", company.getLegalName());
    }
}
