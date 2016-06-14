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
package com.duckheader.stock.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleManager {

    private int hourPoint;
    private int intervalPolicy;
    private TimerTask task;

    public static final int POLICY_BY_DAY = 1;

    public ScheduleManager(int hourPoint, int intervalPolicy, TimerTask task) {
        if (hourPoint > 23 || hourPoint < 0) {
            throw new IllegalArgumentException("Invalid hourPoint argument.");
        }

        if (intervalPolicy != POLICY_BY_DAY) {
            throw new IllegalArgumentException("Invalid intervalPolicy argument.");
        }

        this.hourPoint = hourPoint;
        this.intervalPolicy = intervalPolicy;
        this.task = task;
    }

    public void schedule() {
        Calendar calendar = Calendar.getInstance();
        Date nowDate = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, hourPoint);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date executeDate = calendar.getTime();
        if (executeDate.before(nowDate)) {
            executeDate = addDay(executeDate, 1);
        }

        Timer timer = new Timer();
        timer.schedule(task, executeDate, getPeriod());
    }

    private long getPeriod() {
        if (intervalPolicy == POLICY_BY_DAY) {
            return 3600 * 24;
        } else {
            throw new IllegalStateException("Unknown interval policy");
        }
    }

    private Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }
}
