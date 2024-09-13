package org.bsut.student_sender_bot.service.bot.survey;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Survey {
    SendMessage nextMessage(Long chatId);
    void handleAnswer(Message message);
}
