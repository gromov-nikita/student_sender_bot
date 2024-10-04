package org.bsut.student_sender_bot.dao.specification;

import org.bsut.student_sender_bot.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StudentRecordSpec {

    public Specification<StudentRecord> getId(long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(StudentRecord_.id),
                id
        );
    }
    public Specification<StudentRecord> getDateAfterOrEqually(LocalDate localDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                root.join(StudentRecord_.registration).get(Registration_.date),
                localDate
        );
    }
    public Specification<StudentRecord> getChatId(long chatId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.join(StudentRecord_.appUser).get(AppUser_.chatId),
                chatId
        );
    }
    public Specification<StudentRecord> getDate(LocalDate localDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.join(StudentRecord_.registration).get(Registration_.date),
                localDate
        );
    }
}
