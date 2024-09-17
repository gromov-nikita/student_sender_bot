package org.bsut.student_sender_bot.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;

@RequiredArgsConstructor
@Getter
public enum ConsultationType {
    DEFENSE("Защита"),
    CONSULTATION("Консультация");
    private final String name;
    public static ConsultationType getName(String name) {
        return StreamEx.of(values()).findAny(parameter -> parameter.name.equals(name)).get();
    }
}
