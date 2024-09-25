package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.event.command.StopEvent;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StopHandler {

    private final Bot bot;
    private final SurveyService surveyService;
    private final SendMessageCreator messageCreator;

    @Async
    @EventListener
    public void handle(StopEvent event) {
        Long chatId = event.getMessage().getChatId();
        surveyService.removeSurvey(chatId);
        bot.sendMessage(messageCreator.getDefaultMessage(
                chatId,
                "Вы успешно вышли из опроса. Теперь вы можете вводить команды")
        );
    }
}
