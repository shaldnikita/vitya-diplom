package ru.artemov.victor.diploma.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.entities.operation.OperationType;

import java.util.List;
import java.util.Set;

/**
 * @author Nikita Shaldenkov <shaldnikita2@yandex.ru>
 * on 09.05.2020
 */
public interface OperationRepository extends JpaRepository<Operation, Integer> {
}
