package org.bsut.student_sender_bot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bsut.student_sender_bot.entity.enums.UserType;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(indexes = {
        @Index(name = "index_chatId", columnList = "chatId"),
        @Index(name = "index_phoneNumber", columnList = "phoneNumber"),
        @Index(name = "index_chatId_phoneNumber", columnList = "chatId,phoneNumber")
})
public class AppUser implements Serializable {
    @Transient
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true, nullable = false)
    private long chatId;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "student_group_id")
    private StudentGroup studentGroup;
    @JsonIgnore
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ConsultationTeacher> consultationTeachers;
    @Enumerated(EnumType.STRING)
    private UserType type;
    private String name;

    @Override
    public String toString() {
        return "Ф.И.О.: " + name + "\n" +
                "Группа: " + (Objects.isNull(studentGroup) ? "отсутствует" : studentGroup.getName()) + "\n" +
                "Номер телефона: " + phoneNumber + "\n" +
                "Должность: " + type.getName().toLowerCase() + "\n";
    }

}
