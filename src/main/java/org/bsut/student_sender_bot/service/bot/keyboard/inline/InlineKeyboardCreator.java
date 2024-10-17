package org.bsut.student_sender_bot.service.bot.keyboard.inline;

import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
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

    public InlineKeyboardMarkup createStudentRecordAttendanceInlineKeyboard(List<StudentRecord> studentRecords) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (StudentRecord state : studentRecords) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String buttonText = (state.isAttendance() ? "✅ " : "❌ ") + " " + state.getAppUser().getName();
            button.setText(buttonText);
            button.setCallbackData(CallbackDataPrefix.ATTENDANCE_CHECK.getPrefix()+state.getId()); // Используем id для обработки состояния
            // Создаем строку кнопок (одна кнопка на строке)
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
        }
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        inlineKeyboard.setKeyboard(rows);
        return inlineKeyboard;
    }
}
