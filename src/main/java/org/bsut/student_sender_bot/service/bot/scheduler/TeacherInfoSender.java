package org.bsut.student_sender_bot.service.bot.scheduler;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.ConsultationTeacher;
import org.bsut.student_sender_bot.entity.Registration;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.bsut.student_sender_bot.entity.Teacher;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.StudentSenderBot;
import org.bsut.student_sender_bot.service.data.RegistrationService;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
import org.bsut.student_sender_bot.service.date.DateHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TeacherInfoSender {

    private final RegistrationService registrationService;
    private final DateHandler dateHandler;
    private final DateFormatterCreator dateFormatterCreator;
    private final SendMessageCreator sendMessageCreator;
    private final StudentSenderBot bot;

    @Scheduled(cron = "${cron.send-student.time-interval}")
    @Transactional
    public void sendStudentList() {
        StreamEx.of(registrationService.findAllWithStudentRecordsAndDate(dateHandler.getSaturday())).mapToEntry(
                registration-> StreamEx.of(registration.getConsultation().getConsultationTeachers())
                        .map(ConsultationTeacher::getTeacher)
                        .map(Teacher::getChatId).toList(),
                this::stringify
        ).forEach(this::notice);
    }
    private String stringify(Registration registration) {
        return registration.getDate().format(dateFormatterCreator.getUserLocalDateFormatter()) + " числа " +
                "c " + registration.getConsultation().getStartTime() + " до " + registration.getConsultation().getEndTime()
                + " к вам записаны на консультацию по предмету " + registration.getConsultation().getSubject().getName() + " студенты: " + "\n\n" +
                StreamEx.of(registration.getStudentRecords())
                        .sortedBy(record->record.getStudentGroup().getName())
                        .map(this::stringify).map(str->str+"\n\n").reduce(String::concat).get();
    }
    private String stringify(StudentRecord studentRecord) {
        return studentRecord.getName() + "\n" +
                studentRecord.getStudentGroup().getName() + "\n" +
                studentRecord.getPhoneNumber();
    }
    private void notice(Map.Entry<List<Long>,String> reg) {
        StreamEx.of(reg.getKey()).map(
                teacherChatId -> sendMessageCreator.getDefaultMessage(teacherChatId,reg.getValue())
        ).forEach(bot::sendMessage);
    }
}
