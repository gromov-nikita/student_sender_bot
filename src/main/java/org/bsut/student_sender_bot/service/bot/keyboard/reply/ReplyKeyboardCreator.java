package org.bsut.student_sender_bot.service.bot.keyboard.reply;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.service.bot.StudentSenderBot;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.enums.StudentSenderBotCommand;
import org.bsut.student_sender_bot.service.list_handler.Splitter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReplyKeyboardCreator {
    private final Splitter splitter;

    public <T> ReplyKeyboardMarkup generateReplyKeyboard(List<List<T>> dataMatrix) {
        return getReplyKeyboard(getKeyboardRows(dataMatrix));
    }

    private ReplyKeyboardMarkup getReplyKeyboard(List<KeyboardRow> keyboard) {
        return ReplyKeyboardMarkup.builder().resizeKeyboard(true).oneTimeKeyboard(true).keyboard(keyboard).build();
    }

    private <T> List<KeyboardRow> getKeyboardRows(List<List<T>> dataMatrix) {
        return StreamEx.of(dataMatrix).map(row ->
                StreamEx.of(row).map(Objects::toString).map(KeyboardButton::new).toCollection(KeyboardRow::new)
        ).toList();
    }

    public ReplyKeyboardMarkup generatePhoneNumberReplyKeyboard() {
        return getReplyKeyboard(List.of(new KeyboardRow(
                List.of(KeyboardButton.builder().text("Отправить номер телефона").requestContact(true).build())
        )));
    }
    public ReplyKeyboardMarkup generateCommandsReplyKeyboard(BotCommandLevel botCommandLevel) {
        return getReplyKeyboard(
                getKeyboardRows(splitter.split(
                        StreamEx.of(StudentSenderBotCommand.values())
                                .filter(command->command.getLevel().equals(botCommandLevel))
                                .map(StudentSenderBotCommand::getCommand).toList(),
                        2
                ))
        );
    }
}
