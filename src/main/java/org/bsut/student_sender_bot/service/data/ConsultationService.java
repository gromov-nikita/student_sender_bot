package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.ConsultationRepo;
import org.bsut.student_sender_bot.dao.specification.ConsultationSpec;
import org.bsut.student_sender_bot.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConsultationService {
    private final ConsultationRepo consultationRepo;
    private final ConsultationSpec consultationSpec;

    public List<Consultation> findBySessionAndUser(Session session, AppUser appUser) {
        return consultationRepo.findAll(
                Specification.allOf(
                        consultationSpec.getBySession(session),
                        consultationSpec.getByUser(appUser)
                )
        );
    }
    public Consultation findBySessionAndUserAndSubject(Session session, AppUser appUser, Subject subject) {
        return consultationRepo.findOne(
                Specification.allOf(
                        consultationSpec.getBySession(session),
                        consultationSpec.getByUser(appUser),
                        consultationSpec.getBySubject(subject)
                )
        ).orElseThrow();
    }
    public List<Consultation> getUserConsultations(AppUser appUser) {
        return consultationRepo.findAll(consultationSpec.getByUser(appUser));
    }
}
