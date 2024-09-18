package org.bsut.student_sender_bot.service.date;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

public class DateFormatterCreator {
    public static DateTimeFormatter getUserLocalDateFormatter() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }
}
