package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.ConsultationTeacher;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.bsut.student_sender_bot.entity.Teacher;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.event.command.RegInfoEvent;
import org.bsut.student_sender_bot.service.data.StudentRecordService;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegInfoHandler {

    private final Bot bot;
    private final DateFormatterCreator dateFormatterCreator;
    private final StudentRecordService studentRecordService;
    private final SendMessageCreator messageCreator;

    @Async
    @EventListener
    @Transactional
    public void handle(RegInfoEvent event) {
        Long chatId = event.getMessage().getChatId();
        List<StudentRecord> recordGroup = studentRecordService.findAllByChatIdAndDateAfter(chatId, LocalDate.now());
        if(recordGroup.isEmpty()) bot.sendMessage(messageCreator.getDefaultMessage(chatId,
                "На данный момент у вас отсутствуют запланированные консультации."
        ));
        else bot.sendMessage(messageCreator.getDefaultMessage(chatId,
                "Ваш список запланированных консультаций:\n\n"+
                        StreamEx.of(recordGroup)
                                .sortedBy(record -> record.getRegistration().getDate())
                                .map(this::stringify).map(str->str+"\n").reduce(String::concat).get()
        ));
    }
    private String stringify(StudentRecord studentRecord) {
        return "Дата: " + studentRecord.getRegistration().getDate().format(dateFormatterCreator.getUserLocalDateFormatter()) + ".\n" +
                "C " + studentRecord.getRegistration().getConsultation().getStartTime() +
                " до " + studentRecord.getRegistration().getConsultation().getEndTime() + ".\n" +
                "Предмет: " + studentRecord.getRegistration().getConsultation().getSubject().getName() + ".\n" +
                "Преподаватели:\n" + StreamEx.of(studentRecord.getRegistration().getConsultation().getConsultationTeachers())
                .map(ConsultationTeacher::getTeacher)
                .map(Teacher::getName).map(name->name + "\n").reduce(String::concat).get();
    }
}
