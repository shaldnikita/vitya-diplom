package ru.artemov.victor.diploma.domain.entities.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.artemov.victor.diploma.domain.entities.AbstractEntity;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.domain.entities.user.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Operation extends AbstractEntity {

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

    private LocalDate weightDate;

    private Double weight;

    public Operation(User author) {
        this.author = author;
    }
}
