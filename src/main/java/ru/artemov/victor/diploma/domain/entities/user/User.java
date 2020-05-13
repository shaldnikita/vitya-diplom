package ru.artemov.victor.diploma.domain.entities.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.entities.operation.OperationType;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Nikita Shaldenkov <shaldnikita2@yandex.ru>
 * on 09.05.2020
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private int id = -1;

    private String firstName;

    private String secondName;

    private String login;

    // encoded
    private String password;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private Set<Operation> operations;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_operation_types",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "operation_type_id"))
    private Set<OperationType> operationTypes;

    public boolean isNew() {
        return getId() == -1;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s)", firstName, secondName, login);
    }
}
