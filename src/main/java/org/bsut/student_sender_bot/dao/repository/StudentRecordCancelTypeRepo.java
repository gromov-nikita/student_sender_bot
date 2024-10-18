package org.bsut.student_sender_bot.dao.repository;

import org.bsut.student_sender_bot.entity.AppUser;
import org.bsut.student_sender_bot.entity.StudentRecordCancelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRecordCancelTypeRepo extends JpaRepository<StudentRecordCancelType,Integer>, JpaSpecificationExecutor<StudentRecordCancelType> {

}
