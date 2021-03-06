package ru.artemov.victor.diploma.domain.entities.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.artemov.victor.diploma.domain.entities.AbstractEntity;
import ru.artemov.victor.diploma.domain.entities.user.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@EqualsAndHashCode(exclude = "users")
@Table(
        uniqueConstraints = @UniqueConstraint(name = "name_udx", columnNames = "name")
)
public class OperationType extends AbstractEntity {

    @Size(min = 2, message = "OperationType name must be at least two characters")
    private String name;

    @ManyToMany(mappedBy = "operationTypes", fetch = FetchType.LAZY)
    private Set<User> users;

    @Override
    public String toString() {
        return name;
    }

}
