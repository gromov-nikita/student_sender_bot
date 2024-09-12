package org.bsut.student_sender_bot.service.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BotCommandLevel {
    DEFAULT("Стандартная команда выполняющаяся в исходном состоянии бота."),
    SURVEY("Команда выполняющаяся в процессе опроса.");
    private final String info;
}
