package org.bsut.student_sender_bot.service.bot.survey.update_profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bsut.student_sender_bot.entity.AppUser;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.data.sql.AppUserService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChangePhoneNumberSurvey implements Survey {

    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final AppUserService appUserService;
    private final SendMessageCreator messageCreator;

    private AppUser appUser;
    private String phoneNumber;

    @Override
    public SendMessage nextMessage(Long chatId) {
        if(Objects.isNull(appUser)) appUser = appUserService.getByChatId(chatId);
        if(Objects.isNull(phoneNumber)) return getPhoneNumberMessage(chatId);
        else return null;
    }

    @Override
    public void handleAnswer(Message message) {
        if(Objects.isNull(phoneNumber)) handlePhoneNumberMessage(message);
    }

    @Override
    public SendMessage closeSurvey(Long chatId) {
        appUserService.save(appUser);
        return messageCreator.getReplyKeyboardMessage(
                chatId,
                "Профиль обновлен.",
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, appUser.getType())
        );
    }

    private SendMessage getPhoneNumberMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId, "Поделитесь номером телефона.", getReplyKeyboard());
    }
    private ReplyKeyboardMarkup getReplyKeyboard() {
        List<KeyboardRow> keyboardRows = new LinkedList<>(replyKeyboardCreator.generatePhoneNumberKeyboardRow());
        keyboardRows.addAll(replyKeyboardCreator.generateCommandsKeyboardRows(BotCommandLevel.SURVEY, appUser.getType(),1));
        return replyKeyboardCreator.getReplyKeyboard(keyboardRows,true);
    }
    private void handlePhoneNumberMessage(Message message) {
        this.phoneNumber = message.getContact().getPhoneNumber();
        appUser.setPhoneNumber(phoneNumber);
    }
}
