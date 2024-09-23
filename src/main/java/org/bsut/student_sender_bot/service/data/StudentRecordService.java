package org.bsut.student_sender_bot.service.data;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.StudentRecordRepo;
import org.bsut.student_sender_bot.dao.specification.StudentRecordSpec;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentRecordService {
    private final StudentRecordRepo studentRecordRepo;
    private final StudentRecordSpec studentRecordSpec;
    public StudentRecord save(StudentRecord studentRecord) {
        return studentRecordRepo.save(studentRecord);
    }
    public List<StudentRecord> findAllByChatIdAndDateAfter(long chatId, LocalDate localDate) {
        return studentRecordRepo.findAll(
                Specification.allOf(
                        studentRecordSpec.getChatId(chatId),
                        studentRecordSpec.getDateAfterOrEqually(localDate)
                )
        );
    }
    public long delete(long chatId, long id) {
        return studentRecordRepo.delete(
                Specification.allOf(
                        studentRecordSpec.getId(id),
                        studentRecordSpec.getChatId(chatId)
                )
        );
    }
    public List<StudentRecord> findAllByDate(LocalDate localDate) {
        return studentRecordRepo.findAll(studentRecordSpec.getDate(localDate));
    }
}
