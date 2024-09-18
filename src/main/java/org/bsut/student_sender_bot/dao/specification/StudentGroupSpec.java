package org.bsut.student_sender_bot.dao.specification;

import org.bsut.student_sender_bot.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StudentGroupSpec {
    public Specification<StudentGroup> getStudentGroupsWithConsultationsInSession(Session session) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.join(StudentGroup_.consultationStudentGroups)
                                .join(ConsultationStudentGroup_.consultation)
                                .get(Consultation_.session),
                        session
                );
    }
}
