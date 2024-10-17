package org.bsut.student_sender_bot.service.bot.event.callback;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
public class RegCancelCallbackEvent extends ApplicationEvent {
    private final String callbackData;
    private final Message message;
    public RegCancelCallbackEvent(Object source, String callbackData, Message message) {
        super(source);
        this.callbackData = callbackData;
        this.message = message;
    }
}
