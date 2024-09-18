package org.bsut.student_sender_bot.service.bot.survey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AllRegistrationsSurvey implements Survey {

    private String phoneNumber;

    @Override
    public SendMessage nextMessage(Long chatId) {
        return null;//if(Objects.isNull(phoneNumber)) return generatePhoneNumberReplyKeyboard();
    }

    @Override
    public void handleAnswer(Message message) {

    }

    @Override
    public SendMessage closeSurvey(Long chatId) {
        return null;
    }
}
