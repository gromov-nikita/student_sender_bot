package org.bsut.student_sender_bot.service.bot.survey.update_profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bsut.student_sender_bot.entity.AppUser;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.data.AppUserService;
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
public class ChangeNameSurvey implements Survey {

    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final AppUserService appUserService;
    private final SendMessageCreator messageCreator;

    private AppUser appUser;
    private String name;

    @Override
    public SendMessage nextMessage(Long chatId) {
        if(Objects.isNull(appUser)) appUser = appUserService.getByChatId(chatId);
        if(Objects.isNull(name)) return getNameMessage(chatId);
        else return null;
    }

    @Override
    public void handleAnswer(Message message) {
        if(Objects.isNull(name)) handleNameMessage(message);
    }

    @Override
    public SendMessage closeSurvey(Long chatId) {
        appUserService.save(appUser);
        return messageCreator.getReplyKeyboardMessage(
                chatId,
                "Ф.И.О. успешно изменено.",
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, appUser.getType())
        );
    }
    private SendMessage getNameMessage(Long chatId) {
        return messageCreator.getDefaultMessage(chatId,"Введите ваше Ф.И.О.");
    }
    private void handleNameMessage(Message message) {
        this.name = message.getText();
        appUser.setName(name);

    }
}
