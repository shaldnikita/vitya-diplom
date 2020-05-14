package ru.artemov.victor.diploma.authentication;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.annotation.SessionScope;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;

@SessionScope
@SpringComponent
@RequiredArgsConstructor
public class CurrentUserStorage {

    private User currentUser;

    private final UserRepository userRepository;

    public User getFreshCurrentUser() {
        if (currentUser == null)
            throw new IllegalStateException("No current user set");
        return userRepository.findById(this.currentUser.getId()).get();
    }

    public User getCurrentUser() {
        if (this.currentUser == null)
            throw new IllegalStateException("No current user set");
        return this.currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
