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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

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
        long buttonId = Long.parseLong(event.getCallbackData().replaceAll(ATTENDANCE_CHECK.getPrefix(),""));
        // Получаем текущую клавиатуру
        InlineKeyboardMarkup currentKeyboard = event.getMessage().getReplyMarkup();
        List<List<InlineKeyboardButton>> rows = currentKeyboard.getKeyboard();
        StudentRecord studentRecord = studentRecordService.getById(buttonId);
        studentRecord.setAttendance(!studentRecord.isAttendance());
        studentRecordService.save(studentRecord);
        // Находим кнопку, которую нужно обновить
        for (List<InlineKeyboardButton> row : rows) {
            for (InlineKeyboardButton button : row) {
                if (button.getCallbackData().equals(event.getCallbackData())) {
                    if (button.getText().contains("✅")) button.setText(button.getText().replace("✅","❌")); // Убираем галочку
                    else button.setText(button.getText().replace("❌","✅")); // Ставим галочку
                    break;
                }
            }
        }

        // Создаем объект для обновления клавиатуры
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(event.getMessage().getChatId().toString());
        editMarkup.setMessageId(event.getMessage().getMessageId());
        editMarkup.setReplyMarkup(currentKeyboard);
        // Отправляем обновленную клавиатуру
        try {
            bot.execute(editMarkup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
