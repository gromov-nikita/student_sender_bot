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
    @ManyToMany
    @JoinTable(
            name = "consultation_teachers",
            joinColumns = @JoinColumn(name = "consultation_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> teachers;
    @ManyToMany
    @JoinTable(
            name = "consultation_student_groups",
            joinColumns = @JoinColumn(name = "consultation_id"),
            inverseJoinColumns = @JoinColumn(name = "student_group_id")
    )
    private List<StudentGroup> groups;
    @ManyToOne
    @JoinColumn(nullable = false, name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(nullable = false, name = "session_id")
    private Session session;
    private LocalTime startTime;
    private LocalTime endTime;
}
