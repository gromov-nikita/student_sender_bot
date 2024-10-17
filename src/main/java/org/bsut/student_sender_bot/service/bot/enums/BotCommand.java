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
            "Список всех доступных команд.",
            DEFAULT,
            List.of(UserType.values())
    ),
    PROFILE(
            "/profile",
            "Профиль.",
            DEFAULT,
            List.of(UserType.values())
    ),
    ATTENDANCE_CHECK(
            "/attendanceCheck",
            "Отметить отсутствующих.",
            DEFAULT,
            List.of(UserType.TEACHER)
    ),
    REG(
            "/reg",
            "Регистрация на консультацию.",
            DEFAULT,
            List.of(UserType.STUDENT)
    ),
    REG_INFO(
            "/regInfo",
            "Информации о предстоящих консультациях.",
            DEFAULT,
            List.of(UserType.STUDENT)
    ),
    REG_CANCEL(
            "/regCancel",
            "Отмена консультаций.",
            DEFAULT,
            List.of(UserType.STUDENT)
    ),
    NAME_CHANGE(
            "/nameChange",
            "Смена Ф.И.О.",
            DEFAULT,
            List.of(UserType.values())
    ),
    PHONE_NUMBER_CHANGE(
            "/phoneNumberChange",
            "Смена номера телефона.",
            DEFAULT,
            List.of(UserType.values())
    ),
    STUDENT_GROUP_CHANGE(
            "/studentGroupChange",
            "Смена группы.",
            DEFAULT,
            List.of(UserType.values())
    ),
    STOP(
            "/stop",
            "Прекращение опроса.",
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
    public static List<String> getCommand(UserType userType,BotCommandLevel level) {
        return StreamEx.of(BotCommand.values())
                .filter(command->command.getLevel().equals(level) && command.getUserTypeGroup().contains(userType))
                .map(BotCommand::getCommand).toList();
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
