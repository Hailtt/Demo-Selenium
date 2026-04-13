package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtils {

    public static String getTargetDate(int dayFromNow, String pattern) {
        LocalDate date = LocalDate.now().plusDays(dayFromNow);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        return date.format(formatter);
    }

    public static String formatStringDate(String dateStr, String inputPattern, String outputPattern) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputPattern, Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputPattern, Locale.ENGLISH);
        LocalDate date = LocalDate.parse(dateStr, inputFormatter);
        return date.format(outputFormatter);
    }

}
