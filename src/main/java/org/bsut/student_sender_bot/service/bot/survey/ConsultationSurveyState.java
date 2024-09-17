package org.bsut.student_sender_bot.service.bot.survey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.Consultation;
import org.bsut.student_sender_bot.entity.Session;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.bsut.student_sender_bot.entity.Subject;
import org.bsut.student_sender_bot.entity.enums.ConsultationType;
import org.bsut.student_sender_bot.service.DateParser;
import org.bsut.student_sender_bot.service.data.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.bsut.student_sender_bot.service.bot.survey.SendMessageCreator.*;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConsultationSurveyState implements Survey {

    private final SessionService sessionService;
    private final StudentGroupService studentGroupService;
    private final SubjectService subjectService;
    private final DateParser dateParser;
    private final StudentRecordService studentRecordService;
    private final ConsultationService consultationService;
    private RegistrationService registrationService;

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
    public SendMessage closeSurvey(Long chatId) {
        return getDefaultMessage(chatId, "Вы успешно прошли регистрацию! \nДанные которые вы ввели:\n" + this);
    }
    private SendMessage getDateMessage(Long chatId) {
        this.session = sessionService.getCurrentSession();
        return getReplyKeyboardMessage(chatId,
                "Выберите дату посещения консультации: ",
                generateDateReplyKeyboard(dateParser.getConsultationDateGroup(Pair.of(session.getStartDate(),session.getEndDate())))
        );
    }
    private SendMessage getStudentGroupMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Выберите вашу группу: ",
                generateReplyKeyboard(split(StreamEx.of(studentGroupService.findAll()).map(StudentGroup::getName).sorted().toList(),2))
        );
    }
    private SendMessage getSubjectMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Выберите предмет: ",
                generateReplyKeyboard(split(
                        StreamEx.of(consultationService.findBySessionAndGroup(session,group))
                                .map(Consultation::getSubject).map(Subject::getName).sorted().toList(),
                        1
                ))
        );
    }
    private SendMessage getPhoneNumberMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Поделитесь номером телефона: ",
                generatePhoneNumberReplyKeyboard()
        );
    }
    private SendMessage getTypeMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Выберите цель записи: ",
                generateReplyKeyboard(split(Arrays.stream(ConsultationType.values()).map(ConsultationType::getName).toList(),1))
        );
    }
    private void handleDateMessage(Message message) {
        this.date = LocalDate.parse(message.getText(),DateTimeFormatter.ofPattern("dd-MM-yyyy"));
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
        this.type = ConsultationType.getName(message.getText());
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
                ", \ntypeName: \"" + type.getName() +
                "\n}";
    }
}
