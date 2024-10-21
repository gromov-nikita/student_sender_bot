package org.bsut.student_sender_bot.service.bot.survey.update_profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.AppUser;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.data.sql.AppUserService;
import org.bsut.student_sender_bot.service.data.sql.StudentGroupService;
import org.bsut.student_sender_bot.service.list_handler.Splitter;
import org.springframework.beans.factory.annotation.Value;
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
public class ChangeStudentGroupSurvey implements Survey {

    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final StudentGroupService studentGroupService;
    private final SendMessageCreator messageCreator;
    private final AppUserService appUserService;
    private final Splitter splitter;

    private AppUser appUser;
    private StudentGroup group;

    @Override
    public SendMessage nextMessage(Long chatId) {
        if(Objects.isNull(appUser)) appUser = appUserService.getByChatId(chatId);
        if(Objects.isNull(group)) return getStudentGroupMessage(chatId);
        else return null;
    }

    @Override
    public void handleAnswer(Message message) {
        if(Objects.isNull(group)) handleGroupNameMessage(message);
    }

    @Override
    public SendMessage closeSurvey(Long chatId) {
        appUserService.save(appUser);
        return messageCreator.getReplyKeyboardMessage(
                chatId,
                "Группа изменена.",
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, appUser.getType())
        );
    }
    private SendMessage getStudentGroupMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите вашу группу.",
                getReplyKeyboard()
        );
    }
    private ReplyKeyboardMarkup getReplyKeyboard() {
        List<KeyboardRow> keyboardRows = new LinkedList<>(replyKeyboardCreator.getKeyboardRows(splitter.split(
                StreamEx.of(studentGroupService.findAll()).map(StudentGroup::getName).sorted().toList(),
                2
        )));
        keyboardRows.addAll(replyKeyboardCreator.generateCommandsKeyboardRows(BotCommandLevel.SURVEY, appUser.getType(),1));
        return replyKeyboardCreator.getReplyKeyboard(keyboardRows,true);
    }
    private void handleGroupNameMessage(Message message) {
        String text = message.getText();
        this.group = studentGroupService.findByName(text);
        this.appUser.setStudentGroup(getStudentGroup());
    }
    private StudentGroup getStudentGroup() {
        return Objects.isNull(group.getName()) ? null : group;
    }
}
