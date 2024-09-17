package org.bsut.student_sender_bot.service.bot.survey;

import org.apache.poi.ss.formula.functions.T;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    public static ReplyKeyboardMarkup generateDateReplyKeyboard(List<List<LocalDate>> dateMatrix) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (List<LocalDate> row : dateMatrix) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (LocalDate date : row) {
                KeyboardButton button = new KeyboardButton();
                button.setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                keyboardRow.add(button);
            }
            keyboard.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
    public static <T> ReplyKeyboardMarkup generateReplyKeyboard(List<List<T>> dataMatrix) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (List<T> row : dataMatrix) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (T data : row) {
                KeyboardButton button = new KeyboardButton();
                button.setText(data.toString());
                keyboardRow.add(button);
            }
            keyboard.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup generatePhoneNumberReplyKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton contactButton = new KeyboardButton("Отправить номер телефона");
        contactButton.setRequestContact(true); // Важно!
        row.add(contactButton);
        keyboard.add(row);
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }


}
