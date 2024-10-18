package org.bsut.student_sender_bot.service.bot.callback;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.event.callback.AttendanceCheckCallbackEvent;
import org.bsut.student_sender_bot.service.bot.event.callback.RegCancelCallbackEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix.ATTENDANCE_CHECK;
import static org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix.REG_CANCEL;

@Service
@RequiredArgsConstructor
public class CallbackHandler {

    private final ApplicationEventPublisher publisher;

    public void handle(String callbackData, Message message) {
        if(callbackData.contains(REG_CANCEL.getPrefix())) publisher.publishEvent(new RegCancelCallbackEvent(this,callbackData, message));
        else if(callbackData.contains(ATTENDANCE_CHECK.getPrefix())) publisher.publishEvent(new AttendanceCheckCallbackEvent(this,callbackData, message));
        else throw new IllegalArgumentException("Не поддерживаемый формат данных.");
    }

}
