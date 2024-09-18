package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.ConsultationRepo;
import org.bsut.student_sender_bot.dao.specification.ConsultationSpec;
import org.bsut.student_sender_bot.entity.Consultation;
import org.bsut.student_sender_bot.entity.Session;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.bsut.student_sender_bot.entity.Subject;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationService {
    private final ConsultationRepo consultationRepo;
    private final ConsultationSpec consultationSpec;

    public List<Consultation> findBySessionAndGroup(Session session, StudentGroup group) {
        return consultationRepo.findAll(
                Specification.allOf(
                        consultationSpec.getBySession(session),
                        consultationSpec.getByGroup(group)
                )
        );
    }
    public Consultation findBySessionAndGroupAndSubject(Session session, StudentGroup group, Subject subject) {
        return consultationRepo.findOne(
                Specification.allOf(
                        consultationSpec.getBySession(session),
                        consultationSpec.getByGroup(group),
                        consultationSpec.getBySubject(subject)
                )
        ).orElseThrow();
    }
}
