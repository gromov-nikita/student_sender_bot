package org.bsut.student_sender_bot.service.bot.survey.registration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.*;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
import org.bsut.student_sender_bot.service.date.DateParser;
import org.bsut.student_sender_bot.service.data.*;
import org.bsut.student_sender_bot.service.list_handler.Splitter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegistrationSurvey implements Survey {

    private final SessionService sessionService;
    private final StudentGroupService studentGroupService;
    private final SubjectService subjectService;
    private final DateParser dateParser;
    private final StudentRecordService studentRecordService;
    private final ConsultationService consultationService;
    private final ConsultationTypeService consultationTypeService;
    private final RegistrationService registrationService;
    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final SendMessageCreator messageCreator;
    private final DateFormatterCreator dateFormatterCreator;
    private final Splitter splitter;

    private Session session;
    private LocalDate date;
    private String name;
    private String phoneNumber;
    private StudentGroup group;
    private Subject subject;
    private ConsultationType type;


    @Override
    public SendMessage nextMessage(Long chatId) {
        if (Objects.isNull(date)) return getDateMessage(chatId);
        else if(Objects.isNull(name)) return messageCreator.getDefaultMessage(chatId,"Введите ваше Ф.И.О.");
        else if(Objects.isNull(phoneNumber)) return getPhoneNumberMessage(chatId);
        else if(Objects.isNull(group)) return getStudentGroupMessage(chatId);
        else if(Objects.isNull(subject)) return getSubjectMessage(chatId);
        else if(Objects.isNull(type)) return getTypeMessage(chatId);
        else return null;
    }

    @Override
    public void handleAnswer(Message message) {
        if (Objects.isNull(date)) handleDateMessage(message);
        else if(Objects.isNull(name)) handleNameMessage(message);
        else if(Objects.isNull(phoneNumber)) handlePhoneNumberMessage(message);
        else if(Objects.isNull(group)) handleGroupNameMessage(message);
        else if(Objects.isNull(subject)) handleSubjectNameMessage(message);
        else if(Objects.isNull(type)) handleTypeNameMessage(message);
    }
    @Override
    public SendMessage closeSurvey(Long chatId) {
        StudentRecord record = studentRecordService.save(StudentRecord.builder()
                .chatId(chatId)
                .name(name)
                .studentGroup(group)
                .phoneNumber(phoneNumber)
                .type(type)
                .registration(registrationService.getOrSave(
                        consultationService.findBySessionAndGroupAndSubject(session, group, subject), date)
                ).build()
        );
        return messageCreator.getReplyKeyboardMessage(chatId, "Вы зарегистрированы на " +
                record.getRegistration().getDate().format(dateFormatterCreator.getUserLocalDateFormatter()) + " число. \nПодходите с " +
                record.getRegistration().getConsultation().getStartTime() + " до " +
                record.getRegistration().getConsultation().getEndTime() + "." +
                "\nК преподавателям:\n" + StreamEx.of(record.getRegistration().getConsultation().getConsultationTeachers())
                .map(ConsultationTeacher::getTeacher)
                .map(Teacher::getName).map(name->name + "\n").reduce(String::concat).get(),
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT)
        );
    }
    private SendMessage getDateMessage(Long chatId) {
        this.session = sessionService.getCurrentSession();
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите дату посещения консультации. ",
                replyKeyboardCreator.generateReplyKeyboard(stringify(
                        dateParser.getConsultationDateGroup(Pair.of(session.getStartDate(),session.getEndDate()))
                ))
        );
    }
    private List<List<String>> stringify(List<List<LocalDate>> dateGroup) {
        return StreamEx.of(dateGroup).map(row ->
                StreamEx.of(row).map(date -> date.format(dateFormatterCreator.getUserLocalDateFormatter())).toList()
        ).toList();
    }
    private SendMessage getStudentGroupMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите вашу группу. Если вашей группы нет в списке, значит у группы нет доступных консультаций.",
                replyKeyboardCreator.generateReplyKeyboard(splitter.split(StreamEx.of(
                        studentGroupService.getStudentGroupsWithConsultationsInSession(session)
                ).map(StudentGroup::getName).sorted().toList(),2))
        );
    }
    private SendMessage getSubjectMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите предмет. ",
                replyKeyboardCreator.generateReplyKeyboard(splitter.split(
                        StreamEx.of(consultationService.findBySessionAndGroup(session,group))
                                .map(Consultation::getSubject).map(Subject::getName).sorted().toList(),
                        1
                ))
        );
    }
    private SendMessage getPhoneNumberMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Поделитесь номером телефона. ",
                replyKeyboardCreator.generatePhoneNumberReplyKeyboard()
        );
    }
    private SendMessage getTypeMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите цель записи.",
                replyKeyboardCreator.generateReplyKeyboard(splitter.split(
                        StreamEx.of(consultationTypeService.findAll()).map(ConsultationType::getName).toList(),
                        1
                ))
        );
    }
    private void handleDateMessage(Message message) {
        this.date = LocalDate.parse(message.getText(),dateFormatterCreator.getUserLocalDateFormatter());
    }
    private void handleNameMessage(Message message) {
        this.name = message.getText();
    }
    private void handlePhoneNumberMessage(Message message) {
        this.phoneNumber = message.hasText() ? message.getText() : message.getContact().getPhoneNumber();
    }
    private void handleGroupNameMessage(Message message) {
        this.group = studentGroupService.findByName(message.getText());
    }
    private void handleSubjectNameMessage(Message message) {
        this.subject = subjectService.findByName(message.getText());
    }
    private void handleTypeNameMessage(Message message) {
        this.type = consultationTypeService.findByName(message.getText());
    }
    @Override
    public String toString() {
        return "consultationSurveyState : {" +
                " \ndate: " + date +
                ", \nname: \"" + name + "\"" +
                ", \nphoneNumber: \"" + phoneNumber + "\"" +
                ", \ngroupName: \"" + group.getName() + "\"" +
                ", \nsubjectName: \"" + subject.getName() + "\"" +
                ", \ntypeName: \"" + type.getName() + "\"" +
                "\n}";
    }
}
