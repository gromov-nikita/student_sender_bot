package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.bsut.student_sender_bot.entity.enums.UserType;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix;
import org.bsut.student_sender_bot.service.bot.event.command.RegCancelEvent;
import org.bsut.student_sender_bot.service.bot.keyboard.inline.ButtonData;
import org.bsut.student_sender_bot.service.bot.keyboard.inline.InlineKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.data.AppUserService;
import org.bsut.student_sender_bot.service.data.StudentRecordService;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
import org.bsut.student_sender_bot.service.list_handler.Splitter;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegCancelHandler {

    private final Bot bot;
    private final InlineKeyboardCreator inlineKeyboardCreator;
    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final DateFormatterCreator dateFormatterCreator;
    private final StudentRecordService studentRecordService;
    private final SendMessageCreator messageCreator;
    private final AppUserService appUserService;
    private final Splitter splitter;

    @Async
    @EventListener
    @Transactional
    public void handle(RegCancelEvent event) {
        Long chatId = event.getMessage().getChatId();
        List<StudentRecord> recordGroup = studentRecordService.findAllByChatIdAndDateAfterOrEqually(chatId, LocalDate.now());
        if(recordGroup.isEmpty()) bot.sendMessage(getCancelWithoutRegistrationMessage(chatId));
        else bot.sendMessage(getCancelRegistrationMessage(chatId,recordGroup));
    }
    private SendMessage getCancelWithoutRegistrationMessage(Long chatId) {
        UserType userType = appUserService.getByChatId(chatId).getType();
        return messageCreator.getReplyKeyboardMessage(chatId,
                "На данный момент у вас отсутствуют запланированные консультации.",
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, userType)
        );
    }
    private SendMessage getCancelRegistrationMessage(Long chatId,List<StudentRecord> recordGroup) {
        return messageCreator.getReplyKeyboardMessage(
                chatId,
                "Нажмите на консультации, которые хотите отменить.\n\n",
                getCancelRegistrationKeyboard(recordGroup)
        );
    }
    private ReplyKeyboard getCancelRegistrationKeyboard(List<StudentRecord> recordGroup) {
        return inlineKeyboardCreator.generateInlineKeyboard(
                splitter.split(StreamEx.of(recordGroup).sortedBy(record -> record.getRegistration().getDate())
                                .map(record-> new ButtonData(
                                        stringify(record),
                                        CallbackDataPrefix.REG_CANCEL.getPrefix()+record.getId()
                                )).toList(),
                        1
                )
        );
    }
    private String stringify(StudentRecord studentRecord) {
        return studentRecord.getRegistration().getDate().format(dateFormatterCreator.getUserLocalDateFormatter()) +
                " - " + studentRecord.getRegistration().getConsultation().getSubject().getName() + ".";
    }
}
