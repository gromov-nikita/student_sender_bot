package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.event.command.IdEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdHandler {

    private final Bot bot;
    private final SendMessageCreator messageCreator;

    @Async
    @EventListener
    public void handle(IdEvent event) {
        Long chatId = event.getMessage().getChatId();
        bot.sendMessage(messageCreator.getDefaultMessage(chatId,"Ваш id: " + chatId));
    }
}
