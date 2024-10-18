package org.bsut.student_sender_bot.service.data.sql;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.ConsultationTypeRepo;
import org.bsut.student_sender_bot.entity.ConsultationType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationTypeService {
    private final ConsultationTypeRepo consultationTypeRepo;
    public List<ConsultationType> findAll() {
        return consultationTypeRepo.findAll();
    }
    public ConsultationType findByName(String name) {
        return consultationTypeRepo.findByName(name);
    }
}
