package org.bsut.student_sender_bot.service.bot.survey.callback;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.*;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommand;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.keyboard.inline.InlineKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.data.sql.AppUserService;
import org.bsut.student_sender_bot.service.data.sql.RegistrationService;
import org.bsut.student_sender_bot.service.data.sql.SessionService;
import org.bsut.student_sender_bot.service.data.sql.StudentRecordService;
import org.bsut.student_sender_bot.service.date.DateHandler;
import org.bsut.student_sender_bot.service.list_handler.Splitter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AttendanceCheckSurvey implements Survey {

    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final InlineKeyboardCreator inlineKeyboardCreator;
    private final RegistrationService registrationService;
    private final StudentRecordService studentRecordService;
    private final SendMessageCreator messageCreator;
    private final SessionService sessionService;
    private final AppUserService appUserService;
    private final DateHandler dateHandler;
    private final Splitter splitter;

    private List<Registration> registrations;
    private List<StudentRecord> studentRecords;

    private AppUser appUser;
    private Session session;

    @Override
    @Transactional
    public SendMessage nextMessage(Long chatId) {
        if(Objects.isNull(appUser)) this.appUser = appUserService.getByChatId(chatId);
        if(Objects.isNull(registrations)) return getSubjectNameMessage(chatId);
        else return null;
    }

    @Override
    @Transactional
    public void handleAnswer(Message message) {
        if(Objects.isNull(studentRecords)) handleSubjectNameMessage(message);
    }

    @Override
    public SendMessage closeSurvey(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(
                chatId,
                "Отметьте студентов.",
                inlineKeyboardCreator.createStudentRecordAttendanceInlineKeyboard(studentRecords)
        );
    }
    private SendMessage getSubjectNameMessage(Long chatId) {
        this.session = sessionService.getCurrentSession();
        this.registrations = registrationService.findAllWithNotCanceledStudentRecordsAndLocalDateAndTeacher(dateHandler.getSaturday(), appUser);
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите предмет.",
                replyKeyboardCreator.generateReplyKeyboard(splitter.split(
                        StreamEx.of(registrations).map(Registration::getConsultation).map(Consultation::getSubject).map(Subject::getName)
                                .append(BotCommand.getCommand(appUser.getType(), BotCommandLevel.SURVEY)).toList(),
                        1
                ))
        );
    }
    private void handleSubjectNameMessage(Message message) {
        this.studentRecords = studentRecordService.findAllByRegistration(StreamEx.of(registrations).filter(
                reg->reg.getConsultation().getSubject().getName().equals(message.getText())
        ).findFirst().get());
        studentRecords.forEach(record->Hibernate.initialize(record.getAppUser()));
    }
}
