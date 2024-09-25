package org.bsut.student_sender_bot.service.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel.*;

@RequiredArgsConstructor
@Getter
public enum BotCommand {
    COMMANDS("/commands","Команда, отвечающая за вывод списка всех команд с их описанием.",DEFAULT),
    REG("/reg","Команда, отвечающая за регистрацию на консультацию. После ее ввода начнется опрос.",DEFAULT),
    REG_INFO("/regInfo","Команда, отвечающая за предоставление информации о предстоящих консультациях, на которые вы записаны.",DEFAULT),
    REG_CANCEL("/regCancel","Команда, отвечающая за отображение меню отмены консультаций.",DEFAULT),
    ID("/id","Команда, отвечающая за вывод id вашего telegram.",DEFAULT),
    STOP("/stop","Команда, отвечающая за прекращение опроса.",SURVEY);
    private final String command;
    private final String commandDescription;
    private final BotCommandLevel level;
    public static String getAllCommandInfo() {
        return StreamEx.of(BotCommand.values())
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
