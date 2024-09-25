package org.bsut.student_sender_bot.service.bot.event.command;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
public class StopEvent extends ApplicationEvent {
    private final Message message;
    public StopEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }
}
