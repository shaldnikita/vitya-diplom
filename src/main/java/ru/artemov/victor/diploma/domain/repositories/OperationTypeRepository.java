package ru.artemov.victor.diploma.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemov.victor.diploma.domain.entities.operation.OperationType;

/**
 * @author Nikita Shaldenkov <shaldnikita2@yandex.ru>
 * on 09.05.2020
 */
public interface OperationTypeRepository  extends JpaRepository<OperationType, Integer> {


}
