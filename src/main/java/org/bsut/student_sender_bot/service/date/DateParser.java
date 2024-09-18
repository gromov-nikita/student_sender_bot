package org.bsut.student_sender_bot.service.date;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateParser {
    private static final List<Integer> EVERY_SATURDAY_MONTH_GROUP = List.of(6,9);
    public List<List<LocalDate>> getConsultationDateGroup(Pair<LocalDate,LocalDate> interval) {
        return StreamEx.of(parseIntervalToMonthInterval(interval)).map(this::getSaturdayGroup).toList();
    }
    private List<Pair<LocalDate, LocalDate>> parseIntervalToMonthInterval(Pair<LocalDate, LocalDate> interval) {
        List<Pair<LocalDate, LocalDate>> monthIntervals = new LinkedList<>();
        LocalDate currentStart = interval.getFirst();
        LocalDate monthEnd;
        while (currentStart.isBefore(interval.getSecond()) || currentStart.isEqual(interval.getSecond())) {
            monthEnd = currentStart.withDayOfMonth(currentStart.lengthOfMonth());
            if (monthEnd.isAfter(interval.getSecond())) monthEnd = interval.getSecond();
            monthIntervals.add(Pair.of(currentStart, monthEnd));
            currentStart = monthEnd.plusDays(1);
        }
        return monthIntervals;
    }
    private List<LocalDate> getSaturdayGroup(Pair<LocalDate, LocalDate> interval) {
        if(EVERY_SATURDAY_MONTH_GROUP.contains(interval.getFirst().getMonthValue())) return getEachSaturdays(interval);
        else return getSpecialSaturdays(interval);
    }
    private List<LocalDate> getEachSaturdays(Pair<LocalDate, LocalDate> interval) {
        List<LocalDate> saturdays = new LinkedList<>();
        LocalDate start = interval.getFirst();
        LocalDate end = interval.getSecond();
        LocalDate saturday = start.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        while (!saturday.isAfter(end)) {
            if(saturday.isAfter(LocalDate.now())) saturdays.add(saturday);
            saturday = saturday.plusWeeks(1);
        }
        return saturdays;
    }
    private List<LocalDate> getSpecialSaturdays(Pair<LocalDate, LocalDate> interval) {
        List<LocalDate> saturdays = new LinkedList<>();
        LocalDate saturday = interval.getFirst().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        int saturdayCount = 0;
        while (!saturday.isAfter(interval.getSecond())) {
            saturdayCount++;
            if (saturday.isAfter(LocalDate.now()) && (saturdayCount == 1 || saturdayCount == 3)) saturdays.add(saturday);
            saturday = saturday.plusWeeks(1);
        }
        return saturdays;
    }
}
