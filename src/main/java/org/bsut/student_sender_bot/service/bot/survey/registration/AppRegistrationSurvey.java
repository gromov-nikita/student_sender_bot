package org.bsut.student_sender_bot.service.bot.survey.registration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.AppUser;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.bsut.student_sender_bot.entity.enums.UserType;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.data.StudentGroupService;
import org.bsut.student_sender_bot.service.data.AppUserService;
import org.bsut.student_sender_bot.service.list_handler.Splitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommand.getAllCommandInfo;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AppRegistrationSurvey implements Survey {

    private final Splitter splitter;
    private final SendMessageCreator messageCreator;
    private final StudentGroupService studentGroupService;
    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final AppUserService appUserService;

    private AppUser appUser;
    private boolean newUser = false;
    private boolean changedUser = true;
    private String phoneNumber;
    private String name;
    private StudentGroup group;
    @Value(value = "${const.group.without.name}")
    private String withoutGroupName;

    @Override
    public SendMessage nextMessage(Long chatId) {
        if(Objects.isNull(phoneNumber)) return getPhoneNumberMessage(chatId);
        else if(!newUser) return null;
        else if(Objects.isNull(name)) return getNameMessage(chatId);
        else if(Objects.isNull(group)) return getStudentGroupMessage(chatId);
        else return null;
    }

    @Override
    public void handleAnswer(Message message) {
        if(Objects.isNull(phoneNumber)) handlePhoneNumberMessage(message);
        else if(Objects.isNull(name)) handleNameMessage(message);
        else if(Objects.isNull(group)) handleGroupNameMessage(message);
    }

    @Override
    public SendMessage closeSurvey(Long chatId) {
        if(newUser) this.appUser = AppUser.builder().chatId(chatId).phoneNumber(phoneNumber)
                .studentGroup(group).name(name).type(UserType.STUDENT).build();
        if(changedUser) appUserService.save(appUser);
        return messageCreator.getReplyKeyboardMessage(
                chatId,
                getOnCloseMessage(),
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, appUser.getType())
        );
    }
    private String getOnCloseMessage() {
        return "Здравствуйте, вот ваши данные: \n" + appUser.toString() +
                "\n" + "Вот список всех доступных команд: \n"
                + getAllCommandInfo(appUser.getType());

    }
    private SendMessage getPhoneNumberMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Поделитесь номером телефона.",
                replyKeyboardCreator.generatePhoneNumberReplyKeyboard()
        );
    }
    private SendMessage getStudentGroupMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите вашу группу.",
                replyKeyboardCreator.generateReplyKeyboard(splitter.split(
                        StreamEx.of(studentGroupService.findAll()).map(StudentGroup::getName).append(withoutGroupName).sorted().toList(),
                        2
                ))
        );
    }
    private SendMessage getNameMessage(Long chatId) {
        return messageCreator.getDefaultMessage(chatId,"Введите ваше Ф.И.О.");
    }
    private void handlePhoneNumberMessage(Message message) {
        this.phoneNumber = message.getContact().getPhoneNumber();
        Long chatId = message.getChatId();
        this.appUser = appUserService.getByChatIdOrPhoneNumber(chatId, phoneNumber);
        if(Objects.isNull(appUser)) newUser = true;
        else updateUser(chatId);
    }
    private void updateUser(Long chatId) {
        if(!appUser.getPhoneNumber().equals(phoneNumber)) appUser.setPhoneNumber(phoneNumber);
        else if(appUser.getChatId() != chatId) appUser.setChatId(chatId);
        else changedUser = false;
    }
    private void handleNameMessage(Message message) {
        this.name = message.getText();
    }
    private void handleGroupNameMessage(Message message) {
        String text = message.getText();
        this.group = text.equals(withoutGroupName) ? null : studentGroupService.findByName(text);
    }
}
