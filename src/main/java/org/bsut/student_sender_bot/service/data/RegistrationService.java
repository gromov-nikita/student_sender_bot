package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.RegistrationRepo;
import org.bsut.student_sender_bot.entity.Registration;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepo registrationRepo;

    public Registration save(Registration registration) {
        return registrationRepo.save(registration);
    }
}
