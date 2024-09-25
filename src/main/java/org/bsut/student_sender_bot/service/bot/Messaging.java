package org.bsut.student_sender_bot.service.bot;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.event.survey.SurveyEvent;
import org.bsut.student_sender_bot.service.bot.event.command.*;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommand.*;

@Service
@RequiredArgsConstructor
public class Messaging {

    private final SurveyService surveyService;
    private final ApplicationEventPublisher publisher;

    public void handle(Message message) {
        if(isSurvey(message)) publisher.publishEvent(new SurveyEvent(this,message));
        else handleCommand(message);
    }

    private void handleCommand(Message message) {
        String text = message.getText();
        if(text.equals(REG.getCommand())) publisher.publishEvent(new RegEvent(this,message));
        else if(text.equals(REG_INFO.getCommand())) publisher.publishEvent(new RegInfoEvent(this,message));
        else if(text.equals(REG_CANCEL.getCommand())) publisher.publishEvent(new RegCancelEvent(this,message));
        else if(text.equals(COMMANDS.getCommand())) publisher.publishEvent(new CommandsEvent(this,message));
        else if(text.equals(ID.getCommand())) publisher.publishEvent(new IdEvent(this,message));
        else if(text.equals("/start")) publisher.publishEvent(new StartEvent(this,message));
        else publisher.publishEvent(new IncorrectEvent(this,message));
    }

    private boolean isSurvey(Message message) {
        return surveyService.isContain(message.getChatId());
    }

}
