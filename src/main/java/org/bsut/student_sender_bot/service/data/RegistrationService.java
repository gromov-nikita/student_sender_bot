package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.RegistrationRepo;
import org.bsut.student_sender_bot.dao.specification.RegistrationSpec;
import org.bsut.student_sender_bot.entity.Consultation;
import org.bsut.student_sender_bot.entity.Registration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepo registrationRepo;
    private final RegistrationSpec registrationSpec;

    public Registration save(Registration registration) {
        return registrationRepo.save(registration);
    }
    public Registration getOrSave(Consultation consultation, LocalDate date) {
        Registration registration = findByConsultationAndDate(consultation, date);
        if(Objects.isNull(registration)) return save(Registration.builder().consultation(consultation).date(date).build());
        else return registration;
    }
    public Registration findByConsultationAndDate(Consultation consultation, LocalDate date) {
        return registrationRepo.findByConsultationAndDate(consultation, date);
    }
    public List<Registration> findAllByChatIdAndDateAfter(long chatId, LocalDate localDate) {
        return registrationRepo.findAll(
                Specification.allOf(
                        registrationSpec.getChatId(chatId),
                        registrationSpec.getDateAfterOrEqually(localDate)
                )
        );
    }
    public List<Registration> findAllByDate(LocalDate localDate) {
        return registrationRepo.findAllByDate(localDate);
    }
}
