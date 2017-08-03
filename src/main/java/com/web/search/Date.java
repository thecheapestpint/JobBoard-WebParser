package com.web.search;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Mitchell on 28/07/2017.
 */
public class Date {

    public static long findTimeInHours(int previousTimestamp){
        LocalTime localTime = LocalTime.now();
        Timestamp timestamp = new Timestamp(previousTimestamp);
        LocalTime previousTime = timestamp.toLocalDateTime().toLocalTime();
        return ChronoUnit.HOURS.between(previousTime, localTime);
    }

}
