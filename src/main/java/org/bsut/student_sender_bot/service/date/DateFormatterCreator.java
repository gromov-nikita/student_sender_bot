package org.bsut.student_sender_bot.service.date;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class DateFormatterCreator {
    public DateTimeFormatter getUserLocalDateFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }
}
