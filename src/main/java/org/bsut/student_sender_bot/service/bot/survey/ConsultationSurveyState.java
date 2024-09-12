package org.bsut.student_sender_bot.service.bot.survey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Setter
@Getter
public class ConsultationSurveyState implements Survey {
    private LocalDate date;
    private String name;
    private String phoneNumber;
    private String groupName;
    private String subjectName;
    private String typeName;

    @Override
    public SendMessage nextMessage(Long chatId) {
        if (Objects.isNull(date)) return generateConsultationDate();
        else if(Objects.isNull(name)) return SendMessageCreator.getDefaultMessage(chatId,"Введите ваше Ф.И.О.");
        else if(Objects.isNull(phoneNumber)) return SendMessageCreator.getDefaultMessage(chatId,"Введите ваш номер телефона в формате +375 (**) ***-**-**");
        else if(Objects.isNull(groupName)) return SendMessageCreator.getDefaultMessage(chatId,"Введите название группы");
        else if(Objects.isNull(subjectName)) return null;
        else if(Objects.isNull(typeName)) return null;
        else return null;
    }

    @Override
    public <T> void setState(T value, Consumer<T> setter) {
        setter.accept(value);
    }
    private SendMessage generateConsultationDate() {
        return null;
    }
}
