package org.bsut.student_sender_bot.service.bot.enums;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.enums.UserType;

import java.util.List;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel.*;

@RequiredArgsConstructor
@Getter
public enum BotCommand {
    COMMANDS(
            "/commands",
            "Команда, отвечающая за вывод списка всех команд с их описанием.",
            DEFAULT,
            List.of(UserType.values())
    ),
    REG(
            "/reg",
            "Команда, отвечающая за регистрацию на консультацию. После ее ввода начнется опрос.",
            DEFAULT,
            List.of(UserType.STUDENT)
    ),
    REG_INFO(
            "/regInfo",
            "Команда, отвечающая за предоставление информации о предстоящих консультациях, на которые вы записаны.",
            DEFAULT,
            List.of(UserType.STUDENT)
    ),
    REG_CANCEL(
            "/regCancel",
            "Команда, отвечающая за отображение меню отмены консультаций.",
            DEFAULT,
            List.of(UserType.STUDENT)
    ),
    STOP(
            "/stop",
            "Команда, отвечающая за прекращение опроса.",
            SURVEY,
            List.of(UserType.values())
    );
    private final String command;
    private final String commandDescription;
    private final BotCommandLevel level;
    @NotNull
    private final List<UserType> userTypeGroup;
    public static String getAllCommandInfo() {
        return StreamEx.of(BotCommand.values())
                .map(BotCommand::getInfo)
                .map(info->info+"\n")
                .reduce(String::concat)
                .orElseThrow();
    }
    public static String getAllCommandInfo(UserType userType) {
        return StreamEx.of(BotCommand.values()).filter(command->command.getUserTypeGroup().contains(userType))
                .map(BotCommand::getInfo)
                .map(info->info+"\n")
                .reduce(String::concat)
                .orElseThrow();
    }
    public static String getLevelCommandInfo(BotCommandLevel level) {
        return StreamEx.of(BotCommand.values())
                .filter(command->command.getLevel().equals(level))
                .map(BotCommand::getInfo)
                .map(info->info+"\n")
                .reduce(String::concat)
                .orElseThrow();
    }
    public String getInfo() {
        return command + " - " + commandDescription + " " + level.getInfo();
    }
    public static BotCommand findCommand(String command) {
        for(BotCommand botCommand : BotCommand.values())
            if(botCommand.getCommand().equals(command)) return botCommand;
        return null;
    }
}
