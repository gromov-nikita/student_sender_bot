package org.bsut.student_sender_bot.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@RequiredArgsConstructor
@Getter
public enum ConsultationType {
    DEFENSE("Защита"),
    CONSULTATION("Консультация");
    private final String type;
}
