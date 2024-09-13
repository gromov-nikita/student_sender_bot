package org.bsut.student_sender_bot.dao.specification;

import org.bsut.student_sender_bot.entity.Session;
import org.bsut.student_sender_bot.entity.Session_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SessionSpec {
    public Specification<Session> getInStartDateEndDate(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get(Session_.START_DATE),date),
                        criteriaBuilder.greaterThanOrEqualTo(root.get(Session_.END_DATE),date)
                );
    }
}
