package org.bsut.student_sender_bot.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserType {
    STUDENT("Студент"),
    TEACHER("Преподаватель"),
    SECRETARY("Секретарь");
    private final String name;
}
