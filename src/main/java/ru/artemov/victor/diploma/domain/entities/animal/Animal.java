package ru.artemov.victor.diploma.domain.entities.animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artemov.victor.diploma.domain.entities.AbstractEntity;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author Nikita Shaldenkov <shaldnikita2@yandex.ru>
 * on 09.05.2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(
        uniqueConstraints = @UniqueConstraint(name = "unique_identifier_udx", columnNames = "uniqueIdentifier")
)
public class Animal extends AbstractEntity {

    private String name;

    private LocalDate birthday;

    private LocalDate coming;

    private double weight;

    private LocalDate lastWeightDate;


    private String uniqueIdentifier;

    @OneToMany(mappedBy = "animal", fetch = FetchType.EAGER)
    private Set<Operation> operations;

    @Override
    public String toString() {
        return String.format("%s %s", name, uniqueIdentifier);
    }
}
