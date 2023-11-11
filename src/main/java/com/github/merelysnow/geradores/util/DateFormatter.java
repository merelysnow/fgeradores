package com.github.merelysnow.geradores.util;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

@UtilityClass
public class DateFormatter {

    private final DateFormat DATE_WITH_HOUR_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final Locale LOCALE = new Locale("pt", "BR");
    private final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.LONG, LOCALE);

    public String formatDateWithHour(Date date) {
        return DATE_WITH_HOUR_FORMAT.format(date);
    }

    public String formatDateWithHour(Instant instant) {
        return DATE_WITH_HOUR_FORMAT.format(new Date(instant.toEpochMilli()));
    }

    public String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public String formatDate(Instant instant) {
        return DATE_FORMAT.format(new Date(instant.toEpochMilli()));
    }
}
