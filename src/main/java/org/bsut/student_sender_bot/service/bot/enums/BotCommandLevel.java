package org.bsut.student_sender_bot.service.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BotCommandLevel {
    DEFAULT(""),
    SURVEY(""),
    START("");
    private final String info;
}
