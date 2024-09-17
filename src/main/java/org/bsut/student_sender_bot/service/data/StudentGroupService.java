package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.StudentGroupRepo;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentGroupService {
    private final StudentGroupRepo studentGroupRepo;
    public List<StudentGroup> findAll() {
        return studentGroupRepo.findAll();
    }
    public StudentGroup findByName(String name) {
        return studentGroupRepo.findByName(name);
    }
}
