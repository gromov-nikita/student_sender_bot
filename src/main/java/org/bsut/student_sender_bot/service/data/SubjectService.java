package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.SubjectRepo;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.bsut.student_sender_bot.entity.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepo subjectRepo;
    public List<Subject> findAll() {
        return subjectRepo.findAll();
    }
}
