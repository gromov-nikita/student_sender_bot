package org.bsut.student_sender_bot.service.bot.event.command;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
public class RegInfoEvent extends ApplicationEvent {
    private final Message message;
    public RegInfoEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }
}
