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
            "/команды",
            "Список всех доступных команд.",
            DEFAULT,
            List.of(UserType.values())
    ),
    PROFILE(
            "/профиль",
            "Профиль.",
            DEFAULT,
            List.of(UserType.values())
    ),
    ATTENDANCE_CHECK(
            "/отметить присутствующих",
            "Отметить отсутствующих.",
            DEFAULT,
            List.of(UserType.TEACHER)
    ),
    REG(
            "/зарегистрироваться",
            "Регистрация на консультацию.",
            DEFAULT,
            List.of(UserType.STUDENT)
    ),
    REG_INFO(
            "/информация о регистрациях",
            "Информации о предстоящих консультациях.",
            DEFAULT,
            List.of(UserType.STUDENT)
    ),
    REG_CANCEL(
            "/отмена регистрации",
            "Отмена консультаций.",
            DEFAULT,
            List.of(UserType.STUDENT)
    ),
    NAME_CHANGE(
            "/смена Ф.И.О.",
            "Смена Ф.И.О.",
            DEFAULT,
            List.of(UserType.values())
    ),
    PHONE_NUMBER_CHANGE(
            "/смена номера телефона",
            "Смена номера телефона.",
            DEFAULT,
            List.of(UserType.values())
    ),
    STUDENT_GROUP_CHANGE(
            "/смена группы",
            "Смена группы.",
            DEFAULT,
            List.of(UserType.values())
    ),
    STOP(
            "/прекратить опрос",
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
