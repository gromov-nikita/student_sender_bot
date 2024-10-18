package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.event.command.CommandsEvent;
import org.bsut.student_sender_bot.service.data.sql.AppUserService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommand.getAllCommandInfo;

@Service
@RequiredArgsConstructor
public class CommandsHandler {

    private final Bot bot;
    private final SendMessageCreator messageCreator;
    private final AppUserService appUserService;

    @Async
    @EventListener()
    public void handle(CommandsEvent event) {
        Long chatId = event.getMessage().getChatId();
        bot.sendMessage(messageCreator.getDefaultMessage(chatId,getAllCommandInfo(appUserService.getByChatId(chatId).getType())));
    }
}
