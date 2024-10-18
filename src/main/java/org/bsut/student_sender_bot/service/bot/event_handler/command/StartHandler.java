package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.event.command.StartEvent;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.bsut.student_sender_bot.service.bot.survey.registration.AppRegistrationSurvey;
import org.bsut.student_sender_bot.service.bot.survey.registration.ConsultationRegistrationSurvey;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Objects;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommand.getAllCommandInfo;

@Service
@RequiredArgsConstructor
public class StartHandler {

    private final Bot bot;
    private final SendMessageCreator messageCreator;
    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final ApplicationContext appContext;
    private final SurveyService surveyService;

    @Async
    @EventListener
    public void handle(StartEvent event) {
        Long chatId = event.getMessage().getChatId();
        surveyService.startSurvey(chatId,appContext.getBean(AppRegistrationSurvey.class));
        bot.sendMessage(surveyService.getSurveyState(chatId).nextMessage(chatId));
    }

}
