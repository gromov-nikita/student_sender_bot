package org.bsut.student_sender_bot.dao.criteriaApi;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Root;
import org.bsut.student_sender_bot.entity.Registration;
import org.bsut.student_sender_bot.entity.Registration_;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistrationCriteriaApi {
    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;
    @PostConstruct
    private void initialize() {
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }
    public List<Registration> getWithStudentRecordsAndSpec(Specification<Registration> spec) {
        CriteriaQuery<Registration> query = criteriaBuilder.createQuery(Registration.class);
        Root<Registration> root = query.from(Registration.class);
        ListJoin<Registration, StudentRecord> join = root.join(Registration_.studentRecords);
        return entityManager.createQuery(query.select(root).where(spec.toPredicate(root,query,criteriaBuilder))).getResultList();
    }
}
