package org.bsut.student_sender_bot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bsut.student_sender_bot.entity.enums.ConsultationType;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudentRecord {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String phoneNumber;
    private String groupName;
    @Enumerated(EnumType.STRING)
    private ConsultationType type;
    @ManyToOne
    @JoinColumn(nullable = false, name = "registration_id")
    private Registration registration;
}
