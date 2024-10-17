package org.bsut.student_sender_bot.service.bot.event_handler.callback;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.event.callback.RegCancelCallbackEvent;
import org.bsut.student_sender_bot.service.data.StudentRecordService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommand.getAllCommandInfo;
import static org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix.REG_CANCEL;

@Service
@RequiredArgsConstructor
public class RegCancelCallbackHandler {

    private final Bot bot;
    private final StudentRecordService studentRecordService;
    private final SendMessageCreator messageCreator;

    @Async
    @EventListener()
    public void handle(RegCancelCallbackEvent event) {
        bot.sendMessage(messageCreator.getDefaultMessage(event.getMessage().getChatId(),getMessage(event)));
    }
    private String getMessage(RegCancelCallbackEvent event) {
        long delete = studentRecordService.delete(event.getMessage().getChatId(), Long.parseLong(event.getCallbackData().replaceAll(REG_CANCEL.getPrefix(),"")));
        if(delete == 0) return "Ошибка. Запись уже была удалена.";
        else return "Запись успешно удалена.";
    }
}
