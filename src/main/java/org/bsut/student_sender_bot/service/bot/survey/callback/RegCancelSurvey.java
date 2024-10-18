package org.bsut.student_sender_bot.service.bot.survey.callback;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.AppUser;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.bsut.student_sender_bot.entity.StudentRecordCancelType;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommand;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix;
import org.bsut.student_sender_bot.service.bot.event.callback.RegCancelCallbackEvent;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.data.redis.CallbackDataService;
import org.bsut.student_sender_bot.service.data.sql.AppUserService;
import org.bsut.student_sender_bot.service.data.sql.StudentRecordCancelTypeService;
import org.bsut.student_sender_bot.service.data.sql.StudentRecordService;
import org.bsut.student_sender_bot.service.list_handler.Splitter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Objects;

import static org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix.REG_CANCEL;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegCancelSurvey implements Survey {

    private final StudentRecordCancelTypeService studentRecordCancelTypeService;
    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final CallbackDataService callbackDataService;
    private final StudentRecordService studentRecordService;
    private final SendMessageCreator messageCreator;
    private final AppUserService appUserService;
    private final Splitter splitter;

    private StudentRecordCancelType studentRecordCancelType;

    private AppUser appUser;
    private List<StudentRecordCancelType> studentRecordCancelTypeList;

    @Override
    @Transactional
    public SendMessage nextMessage(Long chatId) {
        if(Objects.isNull(appUser)) this.appUser = appUserService.getByChatId(chatId);
        if(Objects.isNull(studentRecordCancelType)) return getCancelTypeMessage(chatId);
        else return null;
    }

    @Override
    public void handleAnswer(Message message) {
        if(Objects.isNull(studentRecordCancelType)) handleCancelTypeMessage(message);
    }

    @Override
    @Transactional
    public SendMessage closeSurvey(Long chatId) {
        String callbackData = callbackDataService.get(REG_CANCEL, chatId);
        if(Objects.isNull(callbackData)) return messageCreator.getReplyKeyboardMessage(
                chatId,
                "Срок сеанса истек. Нажмите на кнопку консультации, которую хотите отменить еще раз.",
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, appUser.getType())
        );
        else {
            long studentRecordId = Long.parseLong(callbackData.replace(REG_CANCEL.getPrefix(), ""));
            return messageCreator.getReplyKeyboardMessage(
                    chatId,
                    getMessage(studentRecordService.getById(studentRecordId)),
                    replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, appUser.getType())
            );
        }
    }
    private String getMessage(StudentRecord record) {
        if(Objects.isNull(record.getStudentRecordCancelType())) {
            record.setStudentRecordCancelType(studentRecordCancelType);
            studentRecordService.save(record);
            return "Запись успешно отменена.";
        }
        else return "Ошибка. Запись уже была отменена.";
    }
    private SendMessage getCancelTypeMessage(Long chatId) {
        this.studentRecordCancelTypeList = studentRecordCancelTypeService.findAll();
        return messageCreator.getReplyKeyboardMessage(
                chatId,
                "Выберите причину отмены консультации.",
                replyKeyboardCreator.generateReplyKeyboard(splitter.split(
                                StreamEx.of(studentRecordCancelTypeList)
                                        .map(StudentRecordCancelType::getName)
                                        .append(BotCommand.getCommand(appUser.getType(), BotCommandLevel.SURVEY)).toList(),
                                1
                ))
        );
    }
    private void handleCancelTypeMessage(Message message) {
        this.studentRecordCancelType = StreamEx.of(studentRecordCancelTypeList).findFirst(
                type->type.getName().equals(message.getText())
        ).get();
    }

}
