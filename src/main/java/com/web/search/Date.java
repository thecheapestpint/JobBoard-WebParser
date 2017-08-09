package com.web.search;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Mitchell on 28/07/2017.
 */
public class Date {

    public static long findTimeInHours(long previousTimestamp){
        LocalDateTime localTime = LocalDateTime.now();
        Timestamp timestamp = new Timestamp(previousTimestamp);
        LocalDateTime previousTime = timestamp.toLocalDateTime();
        long check = ChronoUnit.HOURS.between(previousTime, localTime);
        return ChronoUnit.HOURS.between(previousTime, localTime);
    }

}
