package org.bsut.student_sender_bot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Registration {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToMany(mappedBy = "registration", fetch = FetchType.LAZY)
    private List<StudentRecord> studentRecords;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "consultation_id")
    private Consultation consultation;
    private LocalDate date;
}
