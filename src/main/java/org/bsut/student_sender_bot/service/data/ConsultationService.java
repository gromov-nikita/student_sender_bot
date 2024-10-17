package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.ConsultationRepo;
import org.bsut.student_sender_bot.dao.specification.ConsultationSpec;
import org.bsut.student_sender_bot.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationService {
    private final ConsultationRepo consultationRepo;
    private final ConsultationSpec consultationSpec;

    public List<Consultation> findBySessionAndStudent(Session session, AppUser student) {
        return consultationRepo.findAll(
                Specification.allOf(
                        consultationSpec.getBySession(session),
                        consultationSpec.getByStudent(student)
                )
        );
    }
    public List<Consultation> findBySessionAndTeacher(Session session, AppUser teacher) {
        return consultationRepo.findAll(
                Specification.allOf(
                        consultationSpec.getBySession(session),
                        consultationSpec.getByTeacher(teacher)
                )
        );
    }
    public Consultation findBySessionAndStudentAndSubject(Session session, AppUser student, Subject subject) {
        return consultationRepo.findOne(
                Specification.allOf(
                        consultationSpec.getBySession(session),
                        consultationSpec.getByStudent(student),
                        consultationSpec.getBySubject(subject)
                )
        ).orElseThrow();
    }
    public List<Consultation> getStudentConsultations(AppUser student) {
        return consultationRepo.findAll(consultationSpec.getByStudent(student));
    }
}
