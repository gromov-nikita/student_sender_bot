package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.StudentGroupRepo;
import org.bsut.student_sender_bot.dao.specification.StudentGroupSpec;
import org.bsut.student_sender_bot.entity.Session;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentGroupService {
    private final StudentGroupRepo studentGroupRepo;
    private final StudentGroupSpec studentGroupSpec;
    public List<StudentGroup> findAll() {
        return studentGroupRepo.findAll();
    }
    public StudentGroup findByName(String name) {
        return studentGroupRepo.findByName(name);
    }
    public List<StudentGroup> getStudentGroupsWithConsultationsInSession(Session session) {
        return studentGroupRepo.findAll(studentGroupSpec.getStudentGroupsWithConsultationsInSession(session));
    }
}
