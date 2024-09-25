package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.event.command.StartEvent;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommand.getAllCommandInfo;

@Service
@RequiredArgsConstructor
public class StartHandler {

    private final Bot bot;
    private final SendMessageCreator messageCreator;
    private final ReplyKeyboardCreator replyKeyboardCreator;

    @Async
    @EventListener
    public void handle(StartEvent event) {
        bot.sendMessage(messageCreator.getReplyKeyboardMessage(
                event.getMessage().getChatId(),
                "Привет, " + event.getMessage().getChat().getFirstName() +
                        ", вот список всех доступных команд: \n" + getAllCommandInfo(),
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT)
        ));
    }
}
