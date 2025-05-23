package org.bsut.student_sender_bot.service.data.sql;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.StudentRecordRepo;
import org.bsut.student_sender_bot.dao.specification.StudentRecordSpec;
import org.bsut.student_sender_bot.entity.AppUser;
import org.bsut.student_sender_bot.entity.Registration;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.bsut.student_sender_bot.entity.Subject;
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
    public List<StudentRecord> findAllNotCanceledByChatIdAndDateAfterOrEqually(long chatId, LocalDate localDate) {
        return studentRecordRepo.findAll(Specification.allOf(
                studentRecordSpec.getChatId(chatId),
                studentRecordSpec.getDateAfterOrEqually(localDate),
                studentRecordSpec.getCancelType(null)
        ));
    }
    public List<StudentRecord> findAllNotCanceledByUserAndDateAfterOrEquallyAndSubject(AppUser appUser, LocalDate localDate, Subject subject) {
        return studentRecordRepo.findAll(Specification.allOf(
                studentRecordSpec.getUser(appUser),
                studentRecordSpec.getDateAfterOrEquallyAndSubject(localDate,subject),
                studentRecordSpec.getCancelType(null)
        ));
    }
    public long delete(long chatId, long id) {
        return studentRecordRepo.delete(
                Specification.allOf(
                        studentRecordSpec.getId(id),
                        studentRecordSpec.getChatId(chatId)
                )
        );
    }
    public List<StudentRecord> findAllByRegistration(Registration registration) {
        return studentRecordRepo.findAll(Specification.allOf(
                studentRecordSpec.getRegistration(registration),
                studentRecordSpec.getCancelType(null)
        ));
    }
    public StudentRecord getById(long id) {
        return studentRecordRepo.findById(id).get();
    }
}
