package ru.artemov.victor.diploma.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;

/**
 * @author Nikita Shaldenkov <shaldnikita2@yandex.ru>
 * on 09.05.2020
 */
public interface AnimalRepository extends JpaRepository<Animal, Integer> {

}
