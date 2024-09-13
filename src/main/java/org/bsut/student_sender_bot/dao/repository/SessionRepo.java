package org.bsut.student_sender_bot.dao.repository;

import org.bsut.student_sender_bot.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SessionRepo extends JpaRepository<Session,Integer>, JpaSpecificationExecutor<Session> {
}
