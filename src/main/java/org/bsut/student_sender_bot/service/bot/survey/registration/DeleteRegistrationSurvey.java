package org.bsut.student_sender_bot.service.bot.survey.registration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bsut.student_sender_bot.service.bot.ReplyKeyBoardCreator;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.data.RegistrationService;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
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
public class DeleteRegistrationSurvey implements Survey {
    private final SendMessageCreator messageCreator;
    private final ReplyKeyBoardCreator replyKeyBoardCreator;
    private final RegistrationService registrationService;
    private final DateFormatterCreator dateFormatterCreator;

    private String phoneNumber;

    @Override
    public SendMessage nextMessage(Long chatId) {return null;}

    @Override
    public void handleAnswer(Message message) {}

    @Override
    public SendMessage closeSurvey(Long chatId) {
        return null;
    }
}
