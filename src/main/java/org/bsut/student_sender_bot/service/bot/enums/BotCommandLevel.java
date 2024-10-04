package org.bsut.student_sender_bot.service.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BotCommandLevel {
    DEFAULT("Стандартная команда выполняющаяся в исходном состоянии помощника."),
    SURVEY("Команда выполняющаяся в процессе опроса."),
    START("Команда начала общения с ботом.");
    private final String info;
}
