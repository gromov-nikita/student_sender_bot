package org.bsut.student_sender_bot.service.bot.keyboard.inline;

import one.util.streamex.StreamEx;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InlineKeyboardCreator {

    public InlineKeyboardMarkup generateInlineKeyboard(List<List<ButtonData>> dataMatrix) {
        return getInlineKeyboard(getInlineKeyboardRow(dataMatrix));
    }

    private InlineKeyboardMarkup getInlineKeyboard(List<List<InlineKeyboardButton>> keyboard) {
        return InlineKeyboardMarkup.builder().keyboard(keyboard).build();
    }

    private List<List<InlineKeyboardButton>> getInlineKeyboardRow(List<List<ButtonData>> dataMatrix) {
        return StreamEx.of(dataMatrix).map(row -> StreamEx.of(row).map(buttonData -> InlineKeyboardButton.builder()
                .text(buttonData.text())
                .callbackData(buttonData.data())
                .build()
        ).toList()).toList();
    }
}
