package ru.ifmo.ab.db.wiki;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class Utils {
    public static int intFromRq(String rq) throws BadRequestException {
        try {
            return Integer.parseInt(rq);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("Could not convert '" + rq + "' to integer value");
        }
    }

    public static Date dateFromRq(String rq) throws BadRequestException {
        try {
            return Date.from(LocalDateTime.parse(rq).atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Could not convert '" + rq + "' to time value");
        }
    }
}
