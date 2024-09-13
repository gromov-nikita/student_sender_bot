package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.SessionRepo;
import org.bsut.student_sender_bot.dao.specification.SessionSpec;
import org.bsut.student_sender_bot.entity.Session;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepo sessionRepo;
    private final SessionSpec sessionSpec;

    public Session getCurrentSession() {
        return sessionRepo.findOne(sessionSpec.getInStartDateEndDate(LocalDate.now())).orElseThrow();
    }
}
