package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.StudentRepo;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentRecordService {
    private final StudentRepo studentRepo;
    public StudentRecord save(StudentRecord studentRecord) {
        return studentRepo.save(studentRecord);
    }
}
