package org.bsut.student_sender_bot.service.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;

import static org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel.*;

@RequiredArgsConstructor
@Getter
public enum StudentSenderBotCommand {
    COMMANDS("/commands","Команда, отвечающая за вывод списка всех команд с их описанием.",DEFAULT),
    REG("/reg","Команда, отвечающая за регистрацию на консультацию. После ее ввода начнется опрос.",DEFAULT),
    ID("/id","Команда, отвечающая за вывод id вашего telegram.",DEFAULT),
    STOP("/stop","Команда, отвечающая за прекращение опроса.",SURVEY);
    private final String command;
    private final String commandDescription;
    private final BotCommandLevel level;
    public static String getAllCommandInfo() {
        return StreamEx.of(StudentSenderBotCommand.values())
                .map(StudentSenderBotCommand::getInfo)
                .map(info->info+"\n")
                .reduce(String::concat)
                .orElseThrow();
    }
    public static String getLevelCommandInfo(BotCommandLevel level) {
        return StreamEx.of(StudentSenderBotCommand.values())
                .filter(command->command.getLevel().equals(level))
                .map(StudentSenderBotCommand::getInfo)
                .map(info->info+"\n")
                .reduce(String::concat)
                .orElseThrow();
    }
    public String getInfo() {
        return command + " - " + commandDescription + " " + level.getInfo();
    }
    public static StudentSenderBotCommand findCommand(String command) {
        for(StudentSenderBotCommand botCommand : StudentSenderBotCommand.values())
            if(botCommand.getCommand().equals(command)) return botCommand;
        return null;
    }
}
