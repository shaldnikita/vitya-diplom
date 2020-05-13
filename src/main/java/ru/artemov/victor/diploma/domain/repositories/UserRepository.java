package ru.artemov.victor.diploma.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemov.victor.diploma.domain.entities.user.User;

import java.util.Optional;

/**
 * @author Nikita Shaldenkov <shaldnikita2@yandex.ru>
 * on 09.05.2020
 */
public interface UserRepository  extends JpaRepository<User, Integer> {

    public Optional<User> findByLogin(String login);
}
