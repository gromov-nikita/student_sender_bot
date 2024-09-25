package org.bsut.student_sender_bot.service.bot.event_handler.survey;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.event.survey.SurveyEvent;
import org.bsut.student_sender_bot.service.bot.event.command.StopEvent;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommand.STOP;

@Service
@RequiredArgsConstructor
public class SurveyHandler {

    private final Bot bot;
    private final SurveyService surveyService;
    private final ApplicationEventPublisher publisher;

    @Async
    @EventListener
    @Transactional
    public void handle(SurveyEvent event) {
        Message message = event.getMessage();
        String text = message.getText();
        if(Objects.nonNull(text) && text.equals(STOP.getCommand())) publisher.publishEvent(new StopEvent(this,message));
        else {
            Survey surveyState = surveyService.getSurveyState(message.getChatId());
            surveyState.handleAnswer(message);
            bot.sendMessage(handleSendMessage(surveyState.nextMessage(message.getChatId()),message.getChatId()));
        }
    }

    private SendMessage handleSendMessage(SendMessage sendMessage, Long chatId) {
        if(Objects.isNull(sendMessage)) return surveyService.removeSurvey(chatId).closeSurvey(chatId);
        else return sendMessage;
    }
}