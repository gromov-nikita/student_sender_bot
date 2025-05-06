package org.bsut.student_sender_bot;

import org.bsut.student_sender_bot.dao.repository.AppUserRepo;
import org.bsut.student_sender_bot.entity.AppUser;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.bsut.student_sender_bot.entity.enums.UserType;
import org.bsut.student_sender_bot.service.data.sql.AppUserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(MockitoExtension.class)
class StudentRecordSenderBotApplicationTests {
    @Mock
    private AppUserRepo userRepo;
    @InjectMocks
    private AppUserService userService;
    @Test
    void shouldCreateUserIfNotExists() {
        long chatId = 12345L;
        String name = "Петя";
        String phoneNumber = "1234567890";
        String userGroup = "PM-41";
        AppUser existingUser = AppUser.builder().chatId(chatId).phoneNumber(phoneNumber)
                .studentGroup(StudentGroup.builder().name(userGroup).build()).name(name).type(UserType.STUDENT).build();
        when(userRepo.findByChatIdOrPhoneNumber(chatId, phoneNumber)).thenReturn(existingUser);
        AppUser result = userService.getByChatIdOrPhoneNumber(chatId, phoneNumber);
        assertEquals(existingUser, result);
        verify(userRepo).findByChatIdOrPhoneNumber(chatId, phoneNumber);
    }
}
