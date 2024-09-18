package org.bsut.student_sender_bot.service.bot;

import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
public class ReplyKeyBoardCreator {

    public <T> ReplyKeyboardMarkup generateReplyKeyboard(List<List<T>> dataMatrix) {
        return getReplyKeyboard(getKeyboardRows(dataMatrix, Objects::toString));
    }

    private ReplyKeyboardMarkup getReplyKeyboard(List<KeyboardRow> keyboard) {
        return ReplyKeyboardMarkup.builder().resizeKeyboard(true).oneTimeKeyboard(true).keyboard(keyboard).build();
    }

    private <T,R> List<KeyboardRow> getKeyboardRows(List<List<T>> dataMatrix, Function<? super T, ? extends R> mapper) {
        return StreamEx.of(dataMatrix).map(row ->
                StreamEx.of(row).map(Objects::toString).map(KeyboardButton::new).toCollection(KeyboardRow::new)
        ).toList();
    }

    public ReplyKeyboardMarkup generatePhoneNumberReplyKeyboard() {
        return getReplyKeyboard(List.of(new KeyboardRow(
                List.of(KeyboardButton.builder().text("Отправить номер телефона").requestContact(true).build())
        )));
    }
}
