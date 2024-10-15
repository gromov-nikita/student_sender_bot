package org.bsut.student_sender_bot.service.bot.event_handler.callback;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.event.callback.RegCancelCallbackEvent;
import org.bsut.student_sender_bot.service.data.StudentRecordService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceCheckCallbackHandler {

    private final Bot bot;
    private final StudentRecordService studentRecordService;
    private final SendMessageCreator messageCreator;

    @Async
    @EventListener()
    public void handle(RegCancelCallbackEvent event) {
        //bot.sendMessage(messageCreator.getDefaultMessage(event.getChatId(),getMessage(event)));
    }
}
