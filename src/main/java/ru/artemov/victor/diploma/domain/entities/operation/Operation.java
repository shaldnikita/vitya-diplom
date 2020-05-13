package ru.artemov.victor.diploma.domain.entities.operation;

import lombok.*;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.domain.entities.user.User;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Operation implements Serializable {

    @NotNull
    @Id
    @GeneratedValue
    private int id = -1;

    @NotNull
    private String name;

    @OneToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private OperationType type;

    @NotNull
    private LocalDate date;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User author;

    @NotNull
    @ManyToOne
    @JoinColumn(name="animal_id", nullable=false)
    private Animal animal;

    public boolean isNew() {
        return getId() == -1;
    }
}
