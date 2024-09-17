package org.bsut.student_sender_bot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToOne
    @JoinColumn(nullable = false, name = "consultation_type_id")
    private ConsultationType type;
    @ManyToOne
    @JoinColumn(nullable = false, name = "registration_id")
    private Registration registration;
}
