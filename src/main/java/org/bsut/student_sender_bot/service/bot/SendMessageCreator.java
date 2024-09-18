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
    private static ReplyKeyboardMarkup getReplyKeyboard(List<KeyboardRow> keyboard) {
        return ReplyKeyboardMarkup.builder().resizeKeyboard(true).oneTimeKeyboard(true).keyboard(keyboard).build();
    }
    public static ReplyKeyboardMarkup generateDateReplyKeyboard(List<List<LocalDate>> dateMatrix) {
        return getReplyKeyboard(getDateKeyboardRows(dateMatrix));
    }
    private static List<KeyboardRow> getDateKeyboardRows(List<List<LocalDate>> dataMatrix) {
        return StreamEx.of(dataMatrix).map(row ->
                StreamEx.of(row).map(date->date.format(getUserLocalDateFormatter())).map(KeyboardButton::new).toCollection(KeyboardRow::new)
        ).toList();
    }
    public static <T> ReplyKeyboardMarkup generateReplyKeyboard(List<List<T>> dataMatrix) {
        return getReplyKeyboard(getKeyboardRows(dataMatrix));
    }
    private static <T> List<KeyboardRow> getKeyboardRows(List<List<T>> dataMatrix) {
        return StreamEx.of(dataMatrix).map(row ->
                StreamEx.of(row).map(Objects::toString).map(KeyboardButton::new).toCollection(KeyboardRow::new)
        ).toList();
    }
    public static ReplyKeyboardMarkup generatePhoneNumberReplyKeyboard() {
        return getReplyKeyboard(List.of(new KeyboardRow(
                List.of(KeyboardButton.builder().text("Отправить номер телефона").requestContact(true).build())
        )));
    }


}
