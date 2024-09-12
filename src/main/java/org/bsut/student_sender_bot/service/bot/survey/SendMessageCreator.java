package org.bsut.student_sender_bot.service.bot.survey;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMessageCreator {
    public static SendMessage getDefault(long chatId, String textToSend) {
        return SendMessage.builder().chatId(String.valueOf(chatId)).text(textToSend).build();
    }
}
