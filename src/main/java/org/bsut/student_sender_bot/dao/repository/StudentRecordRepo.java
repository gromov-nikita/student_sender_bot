package org.bsut.student_sender_bot.dao.repository;

import org.bsut.student_sender_bot.entity.Registration;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRecordRepo extends JpaRepository<StudentRecord,Long>, JpaSpecificationExecutor<StudentRecord> {
    List<StudentRecord> findAllByRegistration(Registration registration);
}
