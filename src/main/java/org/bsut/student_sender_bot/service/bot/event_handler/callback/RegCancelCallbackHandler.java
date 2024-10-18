package org.bsut.student_sender_bot.service.bot.event_handler.callback;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix;
import org.bsut.student_sender_bot.service.bot.event.callback.RegCancelCallbackEvent;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.bsut.student_sender_bot.service.bot.survey.callback.RegCancelSurvey;
import org.bsut.student_sender_bot.service.bot.survey.registration.ConsultationRegistrationSurvey;
import org.bsut.student_sender_bot.service.data.redis.CallbackDataService;
import org.bsut.student_sender_bot.service.data.sql.StudentRecordService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommand.getAllCommandInfo;
import static org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix.REG_CANCEL;

@Service
@RequiredArgsConstructor
public class RegCancelCallbackHandler {

    private final Bot bot;
    private final StudentRecordService studentRecordService;
    private final SendMessageCreator messageCreator;
    private final CallbackDataService callbackDataService;
    private final SurveyService surveyService;
    private final ApplicationContext appContext;

    @Async
    @EventListener()
    public void handle(RegCancelCallbackEvent event) {
        Long chatId = event.getMessage().getChatId();
        callbackDataService.save(REG_CANCEL,event.getMessage().getChatId(),event.getCallbackData());
        surveyService.startSurvey(chatId,appContext.getBean(RegCancelSurvey.class));
        bot.sendMessage(surveyService.getSurveyState(chatId).nextMessage(chatId));

    }
}
