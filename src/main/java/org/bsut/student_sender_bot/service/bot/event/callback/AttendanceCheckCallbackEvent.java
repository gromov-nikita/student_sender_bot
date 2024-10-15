package org.bsut.student_sender_bot.service.bot.event.callback;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class AttendanceCheckCallbackEvent extends ApplicationEvent {
    private final String callbackData;
    private final long chatId;
    public AttendanceCheckCallbackEvent(Object source, String callbackData, long chatId) {
        super(source);
        this.callbackData = callbackData;
        this.chatId = chatId;
    }
}
