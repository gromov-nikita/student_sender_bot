package org.bsut.student_sender_bot.service.bot.callback;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.event.callback.RegCancelCallbackEvent;
import org.bsut.student_sender_bot.service.bot.event.command.RegCancelEvent;
import org.bsut.student_sender_bot.service.data.StudentRecordService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix.ATTENDANCE_CHECK;
import static org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix.REG_CANCEL;

@Service
@RequiredArgsConstructor
public class CallbackHandler {

    private final ApplicationEventPublisher publisher;

    public void handle(String callbackData, long chatId) {
        if(callbackData.contains(REG_CANCEL.getPrefix())) publisher.publishEvent(new RegCancelCallbackEvent(this,callbackData, chatId));
        else if(callbackData.contains(ATTENDANCE_CHECK.getPrefix())) publisher.publishEvent(new RegCancelCallbackEvent(this,callbackData, chatId));
        else throw new IllegalArgumentException("Не поддерживаемый формат данных.");
    }

}
