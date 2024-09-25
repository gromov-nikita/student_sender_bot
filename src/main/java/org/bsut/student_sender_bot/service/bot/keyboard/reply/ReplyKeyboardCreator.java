package org.bsut.student_sender_bot.service.bot.keyboard.reply;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.enums.BotCommand;
import org.bsut.student_sender_bot.service.list_handler.Splitter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReplyKeyboardCreator {
    private final Splitter splitter;

    public <T> ReplyKeyboardMarkup generateReplyKeyboard(List<List<T>> dataMatrix) {
        return getReplyKeyboard(getKeyboardRows(dataMatrix),true);
    }

    private ReplyKeyboardMarkup getReplyKeyboard(List<KeyboardRow> keyboard,boolean oneTimeKeyboard) {
        return ReplyKeyboardMarkup.builder().resizeKeyboard(true).oneTimeKeyboard(oneTimeKeyboard).keyboard(keyboard).build();
    }

    private <T> List<KeyboardRow> getKeyboardRows(List<List<T>> dataMatrix) {
        return StreamEx.of(dataMatrix).map(row ->
                StreamEx.of(row).map(Objects::toString).map(KeyboardButton::new).toCollection(KeyboardRow::new)
        ).toList();
    }

    public ReplyKeyboardMarkup generatePhoneNumberReplyKeyboard() {
        return getReplyKeyboard(List.of(new KeyboardRow(
                List.of(KeyboardButton.builder().text("Отправить номер телефона").requestContact(true).build())
        )),true);
    }
    public ReplyKeyboardMarkup generateCommandsReplyKeyboard(BotCommandLevel botCommandLevel) {
        return getReplyKeyboard(getKeyboardRows(splitter.split(
                StreamEx.of(BotCommand.values())
                        .filter(command->command.getLevel().equals(botCommandLevel))
                        .map(BotCommand::getCommand).toList(),
                2
                )
        ), false);
    }
}
