package org.bsut.student_sender_bot.service.data.sql;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.StudentRecordCancelTypeRepo;
import org.bsut.student_sender_bot.entity.StudentRecordCancelType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentRecordCancelTypeService {
    private final StudentRecordCancelTypeRepo studentRecordCancelTypeRepo;
    public List<StudentRecordCancelType> findAll() {
        return studentRecordCancelTypeRepo.findAll();
    }
}
