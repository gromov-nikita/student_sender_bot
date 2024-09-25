package org.bsut.student_sender_bot.service.bot.event_handler.command;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.BotCommand;
import org.bsut.student_sender_bot.service.bot.event.command.IncorrectEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Objects;
import static org.bsut.student_sender_bot.service.bot.enums.BotCommand.findCommand;

@Service
@RequiredArgsConstructor
public class IncorrectHandler {

    private final Bot bot;
    private final SendMessageCreator messageCreator;

    @Async
    @EventListener
    public void handle(IncorrectEvent event) {
        bot.sendMessage(messageCreator.getDefaultMessage(
                event.getMessage().getChatId(),getIncorrectCommandText(event.getMessage())
        ));
    }
    private String getIncorrectCommandText(Message message) {
        BotCommand botCommand = findCommand(message.getText());
        if(Objects.isNull(botCommand)) return "Извините, " +  message.getChat().getFirstName() + ", я не знаю такой команды.";
        else return "Извините, " +  message.getChat().getFirstName() + ", но эта команда используется в другом состоянии помощника. " + botCommand.getInfo();
    }
}
