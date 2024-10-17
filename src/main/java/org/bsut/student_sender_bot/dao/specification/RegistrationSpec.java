package org.bsut.student_sender_bot.dao.specification;

import org.bsut.student_sender_bot.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RegistrationSpec {

    public Specification<Registration> getDateAfterOrEqually(LocalDate localDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                root.get(Registration_.date),
                localDate
        );
    }
    public Specification<Registration> getDate(LocalDate localDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(Registration_.date),
                localDate
        );
    }
    public Specification<Registration> getTeacher(AppUser appUser) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root
                        .join(Registration_.consultation)
                        .join(Consultation_.consultationTeachers)
                        .get(ConsultationTeacher_.appUser),
                appUser
        );
    }
}
