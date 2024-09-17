package org.bsut.student_sender_bot.dao.repository;

import org.bsut.student_sender_bot.entity.ConsultationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationTypeRepo extends JpaRepository<ConsultationType,Integer>, JpaSpecificationExecutor<ConsultationType> {
    ConsultationType findByName(String name);
}
