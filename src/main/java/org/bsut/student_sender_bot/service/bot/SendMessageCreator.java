package org.bsut.student_sender_bot.service.bot;

import one.util.streamex.StreamEx;
import org.apache.poi.ss.formula.functions.T;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.bsut.student_sender_bot.service.date.DateFormatterCreator.getUserLocalDateFormatter;

public class SendMessageCreator {
    public static SendMessage getDefaultMessage(Long chatId, String textToSend) {
        return SendMessage.builder().chatId(String.valueOf(chatId)).text(textToSend).build();
    }
    public static SendMessage getReplyKeyboardMessage(Long chatId, String textToSend, ReplyKeyboardMarkup keyboard) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .replyMarkup(keyboard)
                .build();
    }
}
