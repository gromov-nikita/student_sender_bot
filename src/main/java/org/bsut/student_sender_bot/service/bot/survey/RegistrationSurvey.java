package org.bsut.student_sender_bot.service.bot.survey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.*;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
import org.bsut.student_sender_bot.service.date.DateParser;
import org.bsut.student_sender_bot.service.data.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.bsut.student_sender_bot.service.bot.ReplyKeyBoardCreator.*;
import static org.bsut.student_sender_bot.service.bot.SendMessageCreator.*;
import static org.bsut.student_sender_bot.service.date.DateFormatterCreator.getUserLocalDateFormatter;

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
        else if(Objects.isNull(name)) return getDefaultMessage(chatId,"Введите ваше Ф.И.О.");
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
    @Transactional
    public SendMessage closeSurvey(Long chatId) {
        StudentRecord record = studentRecordService.save(StudentRecord.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .groupName(group.getName())
                .type(type)
                .registration(registrationService.getOrSave(
                        consultationService.findBySessionAndGroupAndSubject(session, group, subject), date)
                ).build()
        );
        return getDefaultMessage(chatId, "Вы зарегистрированы на " +
                record.getRegistration().getDate().format(getUserLocalDateFormatter()) + " число. \nПодходите с " +
                record.getRegistration().getConsultation().getStartTime() + " до " +
                record.getRegistration().getConsultation().getEndTime() + "." +
                "\nК преподавателям:\n" + StreamEx.of(record.getRegistration().getConsultation().getConsultationTeachers())
                .map(ConsultationTeacher::getTeacher)
                .map(Teacher::getName).map(name->name + "\n").reduce(String::concat).get()
        );
    }
    private SendMessage getDateMessage(Long chatId) {
        this.session = sessionService.getCurrentSession();
        return getReplyKeyboardMessage(chatId,
                "Выберите дату посещения консультации. ",
                generateReplyKeyboard(stringify(
                        dateParser.getConsultationDateGroup(Pair.of(session.getStartDate(),session.getEndDate()))
                ))
        );
    }
    private List<List<String>> stringify(List<List<LocalDate>> dateGroup) {
        return StreamEx.of(dateGroup).map(row ->
                StreamEx.of(row).map(date -> date.format(getUserLocalDateFormatter())).toList()
        ).toList();
    }
    private SendMessage getStudentGroupMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Выберите вашу группу. Если вашей группы нет в списке, значит у группы нет доступных консультаций.",
                generateReplyKeyboard(split(StreamEx.of(
                        studentGroupService.getStudentGroupsWithConsultationsInSession(session)
                ).map(StudentGroup::getName).sorted().toList(),2))
        );
    }
    private SendMessage getSubjectMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Выберите предмет. ",
                generateReplyKeyboard(split(
                        StreamEx.of(consultationService.findBySessionAndGroup(session,group))
                                .map(Consultation::getSubject).map(Subject::getName).sorted().toList(),
                        1
                ))
        );
    }
    private SendMessage getPhoneNumberMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Поделитесь номером телефона. ",
                generatePhoneNumberReplyKeyboard()
        );
    }
    private SendMessage getTypeMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Выберите цель записи. ",
                generateReplyKeyboard(split(StreamEx.of(consultationTypeService.findAll()).map(ConsultationType::getName).toList(),1))
        );
    }
    private void handleDateMessage(Message message) {
        this.date = LocalDate.parse(message.getText(),getUserLocalDateFormatter());
    }
    private void handleNameMessage(Message message) {
        this.name = message.getText();
    }
    private void handlePhoneNumberMessage(Message message) {
        this.phoneNumber = "+" + message.getContact().getPhoneNumber();
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
    private <T> List<List<T>> split(List<T> list, Integer chunkSize) {
        return IntStream.range(0, (list.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> list.subList(i * chunkSize, Math.min(i * chunkSize + chunkSize, list.size())))
                .collect(Collectors.toList());
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
