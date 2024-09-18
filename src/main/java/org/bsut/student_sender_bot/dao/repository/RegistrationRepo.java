package org.bsut.student_sender_bot.dao.repository;

import org.bsut.student_sender_bot.entity.Consultation;
import org.bsut.student_sender_bot.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RegistrationRepo extends JpaRepository<Registration,Integer>, JpaSpecificationExecutor<Registration> {
    Registration findByConsultationAndDate(Consultation consultation, LocalDate localDate);
}
