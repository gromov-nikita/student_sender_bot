package org.bsut.student_sender_bot.dao.repository;

import org.bsut.student_sender_bot.entity.Session;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGroupRepo extends JpaRepository<StudentGroup,Integer>, JpaSpecificationExecutor<StudentGroup> {
}
