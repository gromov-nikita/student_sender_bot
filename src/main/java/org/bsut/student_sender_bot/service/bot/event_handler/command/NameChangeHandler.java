package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.event.command.NameChangeEvent;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.bsut.student_sender_bot.service.bot.survey.update_profile.ChangeNameSurvey;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NameChangeHandler {

    private final ApplicationContext appContext;
    private final SurveyService surveyService;
    private final Bot bot;

    @Async
    @EventListener
    public void handle(NameChangeEvent event) {
        Long chatId = event.getMessage().getChatId();
        surveyService.startSurvey(chatId,appContext.getBean(ChangeNameSurvey.class));
        handleSendMessage(surveyService.getSurveyState(chatId).nextMessage(chatId), chatId);
    }
    private void handleSendMessage(SendMessage sendMessage, Long chatId) {
        if(Objects.isNull(sendMessage)) bot.sendMessage(surveyService.removeSurvey(chatId).closeSurvey(chatId));
        else bot.sendMessage(sendMessage);
    }
}
