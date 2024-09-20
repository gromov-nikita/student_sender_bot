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
    private long id;
    private String name;
    private String phoneNumber;
    private long chatId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "consultation_type_id")
    private ConsultationType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "registration_id")
    private Registration registration;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "student_group_id")
    private StudentGroup studentGroup;
}
