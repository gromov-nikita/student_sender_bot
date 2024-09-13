package org.bsut.student_sender_bot.service.bot.survey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.Session;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.bsut.student_sender_bot.entity.Subject;
import org.bsut.student_sender_bot.entity.enums.ConsultationType;
import org.bsut.student_sender_bot.service.DateParser;
import org.bsut.student_sender_bot.service.data.SessionService;
import org.bsut.student_sender_bot.service.data.StudentGroupService;
import org.bsut.student_sender_bot.service.data.SubjectService;
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
import java.util.function.Consumer;
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

    private LocalDate date;
    private String name;
    private String phoneNumber;
    private String groupName;
    private String subjectName;
    private String typeName;


    @Override
    public SendMessage nextMessage(Long chatId) {
        if (Objects.isNull(date)) return getDateMessage(chatId);
        else if(Objects.isNull(name)) return getDefaultMessage(chatId,"Введите ваше Ф.И.О.");
        else if(Objects.isNull(phoneNumber)) return getDefaultMessage(chatId,"Введите ваш номер телефона в формате +375 (**) ***-**-**");
        else if(Objects.isNull(groupName)) return getStudentGroupMessage(chatId);
        else if(Objects.isNull(subjectName)) return getSubjectMessage(chatId);
        else if(Objects.isNull(typeName)) return getTypeMessage(chatId);
        else return null;
    }

    @Override
    public void handleAnswer(Message message) {
        if (Objects.isNull(date)) handleDateMessage(message);
        else if(Objects.isNull(name)) handleNameMessage(message);
        else if(Objects.isNull(phoneNumber)) handlePhoneNumberMessage(message);
        else if(Objects.isNull(groupName)) handleGroupNameMessage(message);
        else if(Objects.isNull(subjectName)) handleSubjectNameMessage(message);
        else if(Objects.isNull(typeName)) handleTypeNameMessage(message);
    }
    private SendMessage getDateMessage(Long chatId) {
        Session currentSession = sessionService.getCurrentSession();
        return getDateReplyKeyboardMessage(chatId,
                "Выберите дату посещения консультации: ",
                dateParser.getConsultationDateGroup(Pair.of(currentSession.getStartDate(),currentSession.getEndDate()))
        );
    }
    private SendMessage getStudentGroupMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Выберите вашу группу: ",
                split(StreamEx.of(studentGroupService.findAll()).map(StudentGroup::getName).sorted().toList(),2)
        );
    }
    private SendMessage getSubjectMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Выберите предмет: ",
                split(StreamEx.of(subjectService.findAll()).map(Subject::getName).sorted().toList(),1)
        );
    }
    private SendMessage getTypeMessage(Long chatId) {
        return getReplyKeyboardMessage(chatId,
                "Выберите цель записи: ",
                split(Arrays.stream(ConsultationType.values()).map(ConsultationType::getType).toList(),1)
        );
    }
    private void handleDateMessage(Message message) {
        this.date = LocalDate.parse(message.getText(),DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
    private void handleNameMessage(Message message) {
        this.name = message.getText();
    }
    private void handlePhoneNumberMessage(Message message) {
        this.phoneNumber = message.getText();
    }
    private void handleGroupNameMessage(Message message) {
        this.groupName = message.getText();
    }
    private void handleSubjectNameMessage(Message message) {
        this.subjectName = message.getText();
    }
    private void handleTypeNameMessage(Message message) {
        this.typeName = message.getText();
    }
    private <T> List<List<T>> split(List<T> list, Integer chunkSize) {
        return IntStream.range(0, (list.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> list.subList(i * chunkSize, Math.min(i * chunkSize + chunkSize, list.size())))
                .collect(Collectors.toList());
    }
    @Override
    public String toString() {
        return "consultationSurveyState : {" +
                ", \ndate: " + date + "," +
                ", \nname: \"" + name + "\"," +
                ", \nphoneNumber: \"" + phoneNumber + "\"," +
                ", \ngroupName: \"" + groupName + "\"," +
                ", \nsubjectName: \"" + subjectName + "\"," +
                ", \ntypeName: \"" + typeName + "\"," +
                "\n}";
    }
}
