package org.bsut.student_sender_bot.service.bot.event_handler.callback;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.bsut.student_sender_bot.service.bot.Bot;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.event.callback.AttendanceCheckCallbackEvent;
import org.bsut.student_sender_bot.service.data.sql.StudentRecordService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Objects;

import static org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix.ATTENDANCE_CHECK;

@Service
@RequiredArgsConstructor
public class AttendanceCheckCallbackHandler {

    private final Bot bot;
    private final StudentRecordService studentRecordService;
    private final SendMessageCreator messageCreator;

    @Async
    @EventListener()
    public void handle(AttendanceCheckCallbackEvent event) {
        String callbackData = event.getCallbackData();
        InlineKeyboardMarkup keyboard = event.getMessage().getReplyMarkup();
        updateStudentRecordAttendance(callbackData);
        swapIcon(getButtonByCallbackData(keyboard, callbackData));
        bot.updateMessage(getEditMessageReplyMarkup(event.getMessage(),keyboard));
    }
    private EditMessageReplyMarkup getEditMessageReplyMarkup(Message message, InlineKeyboardMarkup keyboard) {
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(message.getChatId().toString());
        editMarkup.setMessageId(message.getMessageId());
        editMarkup.setReplyMarkup(keyboard);
        return editMarkup;
    }
    private InlineKeyboardButton getButtonByCallbackData(InlineKeyboardMarkup keyboard, String callbackData) {
        for (List<InlineKeyboardButton> row : keyboard.getKeyboard()) {
            for (InlineKeyboardButton button : row) if (button.getCallbackData().equals(callbackData)) return button;
        }
        return null;
    }
    private void swapIcon(InlineKeyboardButton button) {
        if(Objects.nonNull(button)) {
            if (button.getText().contains("✅")) button.setText(button.getText().replace("✅", "❌"));
            else button.setText(button.getText().replace("❌", "✅"));
        }
    }
    private void updateStudentRecordAttendance(String callbackData) {
        long studentRecordId = Long.parseLong(callbackData.replaceAll(ATTENDANCE_CHECK.getPrefix(),""));
        StudentRecord studentRecord = studentRecordService.getById(studentRecordId);
        studentRecord.setAttendance(!studentRecord.isAttendance());
        studentRecordService.save(studentRecord);
    }
}
