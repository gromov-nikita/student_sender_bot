package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.entity.AppUser;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.event.command.ProfileEvent;
import org.bsut.student_sender_bot.service.bot.event.command.RegCancelEvent;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.data.AppUserService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileHandler {

    private final Bot bot;
    private final AppUserService appUserService;
    private final SendMessageCreator messageCreator;
    private final ReplyKeyboardCreator replyKeyboardCreator;

    @Async
    @EventListener
    @Transactional
    public void handle(ProfileEvent event) {
        Long chatId = event.getMessage().getChatId();
        AppUser user = appUserService.getByChatId(chatId);
        bot.sendMessage(messageCreator.getReplyKeyboardMessage(chatId,
                user.toString(),
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, user.getType())
        ));
    }
}
