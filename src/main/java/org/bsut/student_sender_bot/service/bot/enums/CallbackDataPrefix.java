package org.bsut.student_sender_bot.service.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CallbackDataPrefix {
    REG_CANCEL("REG_CANCEL_"),
    ATTENDANCE_CHECK("ATTENDANCE_CHECK_");
    private final String prefix;
}
