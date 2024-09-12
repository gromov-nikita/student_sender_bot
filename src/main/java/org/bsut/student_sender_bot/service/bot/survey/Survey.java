package org.bsut.student_sender_bot.service.bot.survey;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Consumer;

public interface Survey {
    SendMessage nextMessage(Long chatId);
    <T> void setState(T value, Consumer<T> setter);
}
