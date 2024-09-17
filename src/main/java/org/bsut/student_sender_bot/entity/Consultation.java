package org.bsut.student_sender_bot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Consultation {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultationTeacher> consultationTeachers;
    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultationStudentGroup> consultationStudentGroups;
    @ManyToOne
    @JoinColumn(nullable = false, name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(nullable = false, name = "session_id")
    private Session session;
    private LocalTime startTime;
    private LocalTime endTime;
}
