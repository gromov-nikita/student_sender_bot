package org.bsut.student_sender_bot.service.bot.callback;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix;
import org.bsut.student_sender_bot.service.data.StudentRecordService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix.REG_CANCEL;

@Service
@RequiredArgsConstructor
public class CallbackHandler {

    private final StudentRecordService studentRecordService;
    private final SendMessageCreator messageCreator;

    public SendMessage getAnswer(String callbackData, long chatId) {
        if(callbackData.contains(REG_CANCEL.getPrefix())) return cancelRegistration(callbackData, chatId);
        throw new IllegalArgumentException("Не поддерживаемый формат данных.");
    }
    private SendMessage cancelRegistration(String callbackData, long chatId) {
        long delete = studentRecordService.delete(chatId, Long.parseLong(callbackData.replaceAll(REG_CANCEL.getPrefix(),"")));
        if(delete == 0) return messageCreator.getDefaultMessage(chatId,"Ошибка. Запись уже была удалена.");
        else return messageCreator.getDefaultMessage(chatId,"Запись успешно удалена.");
    }
}
