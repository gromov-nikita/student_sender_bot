package org.bsut.student_sender_bot.service.bot.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Message;

@Getter
public class SurveyEvent extends ApplicationEvent {
    private final Message message;
    public SurveyEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }
}
