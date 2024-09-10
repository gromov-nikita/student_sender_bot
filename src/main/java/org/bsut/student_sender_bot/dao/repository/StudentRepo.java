package org.bsut.student_sender_bot.dao.repository;

import org.bsut.student_sender_bot.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student,Integer>, JpaSpecificationExecutor<Student> {
}
