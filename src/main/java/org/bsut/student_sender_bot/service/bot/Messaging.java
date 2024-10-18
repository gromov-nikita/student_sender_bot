package org.bsut.student_sender_bot.service.bot;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.entity.AppUser;
import org.bsut.student_sender_bot.entity.enums.UserType;
import org.bsut.student_sender_bot.service.bot.enums.BotCommand;
import org.bsut.student_sender_bot.service.bot.event.survey.SurveyEvent;
import org.bsut.student_sender_bot.service.bot.event.command.*;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.bsut.student_sender_bot.service.data.sql.AppUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommand.*;

@Service
@RequiredArgsConstructor
public class Messaging {

    private final SurveyService surveyService;
    private final ApplicationEventPublisher publisher;
    private final AppUserService appUserService;

    public void handle(Message message) {
        if(isSurvey(message)) publisher.publishEvent(new SurveyEvent(this,message));
        else if(isAccessible(message)) handleCommand(message);
    }
    private boolean isAccessible(Message message) {
        if(message.getText().equals("/start")) return true;
        BotCommand command = findCommand(message.getText());
        if (Objects.nonNull(command)) return command.getUserTypeGroup().contains(getUserType(message.getChatId()));
        else return false;
    }
    private UserType getUserType(Long chatId) {
        AppUser appUser = appUserService.getByChatId(chatId);
        return Objects.isNull(appUser) ? UserType.STUDENT : appUser.getType();
    }
    private void handleCommand(Message message) {
        String text = message.getText();
        if(text.equals(REG.getCommand())) publisher.publishEvent(new RegEvent(this,message));
        else if(text.equals(REG_INFO.getCommand())) publisher.publishEvent(new RegInfoEvent(this,message));
        else if(text.equals(REG_CANCEL.getCommand())) publisher.publishEvent(new RegCancelEvent(this,message));
        else if(text.equals(ATTENDANCE_CHECK.getCommand())) publisher.publishEvent(new AttendanceCheckEvent(this,message));
        else if(text.equals(PHONE_NUMBER_CHANGE.getCommand())) publisher.publishEvent(new PhoneNumberChangeEvent(this,message));
        else if(text.equals(STUDENT_GROUP_CHANGE.getCommand())) publisher.publishEvent(new StudentGroupChangeEvent(this,message));
        else if(text.equals(NAME_CHANGE.getCommand())) publisher.publishEvent(new NameChangeEvent(this,message));
        else if(text.equals(PROFILE.getCommand())) publisher.publishEvent(new ProfileEvent(this,message));
        else if(text.equals(COMMANDS.getCommand())) publisher.publishEvent(new CommandsEvent(this,message));
        else if(text.equals("/start")) publisher.publishEvent(new StartEvent(this,message));
        else publisher.publishEvent(new IncorrectEvent(this,message));
    }

    private boolean isSurvey(Message message) {
        return surveyService.isContain(message.getChatId());
    }

}
