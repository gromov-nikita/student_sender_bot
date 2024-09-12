package org.bsut.student_sender_bot.service.bot.survey;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMessageCreator {
    public static SendMessage getDefaultMessage(long chatId, String textToSend) {
        return SendMessage.builder().chatId(String.valueOf(chatId)).text(textToSend).build();
    }
}
