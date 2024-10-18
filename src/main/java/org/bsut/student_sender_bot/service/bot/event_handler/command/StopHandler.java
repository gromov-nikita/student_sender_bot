package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.entity.enums.UserType;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.event.command.StopEvent;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.bsut.student_sender_bot.service.bot.survey.registration.AppRegistrationSurvey;
import org.bsut.student_sender_bot.service.data.sql.AppUserService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StopHandler {

    private final Bot bot;
    private final SurveyService surveyService;
    private final AppUserService appUserService;
    private final SendMessageCreator messageCreator;
    private final ReplyKeyboardCreator replyKeyboardCreator;

    @Async
    @EventListener
    public void handle(StopEvent event) {
        Long chatId = event.getMessage().getChatId();
        UserType userType = appUserService.getByChatId(chatId).getType();
        if(surveyService.getSurveyState(chatId) instanceof AppRegistrationSurvey) return;
        surveyService.removeSurvey(chatId);
        bot.sendMessage(messageCreator.getReplyKeyboardMessage(
                chatId,
                "Вы успешно вышли из опроса. Теперь вы можете вводить команды",
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, userType)
        ));
    }
}
