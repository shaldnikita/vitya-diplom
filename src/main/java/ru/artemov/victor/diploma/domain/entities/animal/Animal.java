package ru.artemov.victor.diploma.domain.entities.animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Nikita Shaldenkov <shaldnikita2@yandex.ru>
 * on 09.05.2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Animal {

    @Id
    @GeneratedValue
    private int id = -1;

    private String name;

    private String uniqueIdentifier;

    @OneToMany(mappedBy = "animal", fetch = FetchType.EAGER)
    private Set<Operation> operations;

    public boolean isNew() {
        return getId() == -1;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, uniqueIdentifier);
    }
}
