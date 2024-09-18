package org.bsut.student_sender_bot.dao.specification;

import org.bsut.student_sender_bot.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RegistrationSpec {
    public Specification<Registration> getPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.join(Registration_.studentRecords).get(StudentRecord_.phoneNumber),
                phoneNumber
        );
    }
    public Specification<Registration> getDateAfterOrEqually(LocalDate localDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                root.get(Registration_.date),
                localDate
        );
    }
}
