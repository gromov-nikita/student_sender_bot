package org.bsut.student_sender_bot.dao.specification;

import org.bsut.student_sender_bot.entity.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ConsultationSpec {
    public Specification<Consultation> getByGroup(StudentGroup group) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join(Consultation_.consultationStudentGroups).get(ConsultationStudentGroup_.studentGroup),group);
    }
    public Specification<Consultation> getBySession(Session session) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Consultation_.session),session);
    }
    public Specification<Consultation> getBySubject(Subject subject) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Consultation_.subject),subject);
    }

    public Specification<Consultation> getByStudent(AppUser student) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(student,
                root
                        .join(Consultation_.consultationStudentGroups)
                        .join(ConsultationStudentGroup_.studentGroup)
                        .get(StudentGroup_.appUsers)
        );
    }
    public Specification<Consultation> getByTeacher(AppUser teacher) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root
                        .join(Consultation_.consultationTeachers)
                        .get(ConsultationTeacher_.appUser),
                teacher
        );
    }


}
