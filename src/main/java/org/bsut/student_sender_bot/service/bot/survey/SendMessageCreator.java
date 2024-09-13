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
    public static <T> SendMessage getInlineKeyboardMessage(Long chatId, String textToSend,List<List<T>> dateMatrix) {
        return SendMessage.builder().chatId(chatId).text(textToSend).replyMarkup(generateInlineKeyboard(dateMatrix)).build();
    }
    private static <T> InlineKeyboardMarkup generateInlineKeyboard(List<List<T>> dataMatrix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (List<T> row : dataMatrix) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            for (T data : row) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(data.toString());
                button.setCallbackData("data:" + data);
                keyboardRow.add(button);
            }
            keyboard.add(keyboardRow);
        }
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }
    public static SendMessage getDateInlineKeyboardMessage(Long chatId, String textToSend,List<List<LocalDate>> dateMatrix) {
        return SendMessage.builder().chatId(chatId).text(textToSend).replyMarkup(generateDateInlineKeyboard(dateMatrix)).build();
    }
    private static InlineKeyboardMarkup generateDateInlineKeyboard(List<List<LocalDate>> dateMatrix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (List<LocalDate> row : dateMatrix) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            for (LocalDate date : row) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                button.setCallbackData("data:" + date);
                keyboardRow.add(button);
            }
            keyboard.add(keyboardRow);
        }
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }


    public static SendMessage getDateReplyKeyboardMessage(Long chatId, String textToSend, List<List<LocalDate>> dateMatrix) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .replyMarkup(generateDateReplyKeyboard(dateMatrix))
                .build();
    }

    private static ReplyKeyboardMarkup generateDateReplyKeyboard(List<List<LocalDate>> dateMatrix) {
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
    public static <T> SendMessage getReplyKeyboardMessage(Long chatId, String textToSend, List<List<T>> dataMatrix) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .replyMarkup(generateReplyKeyboard(dataMatrix))
                .build();
    }

    private static <T> ReplyKeyboardMarkup generateReplyKeyboard(List<List<T>> dataMatrix) {
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


}
