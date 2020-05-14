package ru.artemov.victor.diploma.authentication;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;

/**
 * Default mock implementation of {@link AccessControl}. This implementation
 * accepts any string as a user if the password is the same string, and
 * considers the user "admin" as the only administrator.
 */
@Service
@RequiredArgsConstructor
public class BasicAccessControl implements AccessControl {

    private final UserRepository userRepository;

    private final CurrentUserStorage currentUserStorage;

    @Override
    public boolean signIn(String username, String password) {
        if (username == null || username.isEmpty()) {
            return false;
        }

        if (password == null || password.isEmpty()) {
            return false;
        }

        var maybeUser = userRepository.findByLogin(username);
        if (maybeUser.filter(user -> user.getPassword().equals(password)).isPresent()) {
            currentUserStorage.setCurrentUser(maybeUser.get());
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean isUserSignedIn() {
        try {
            currentUserStorage.getCurrentUser();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isUserInRole(String role) {
        if ("va".equals(role)) {
            // Only the "admin" user is in the "admin" role
            return getPrincipalName().equals("va");
        }

        // All users are in all non-admin roles
        return true;
    }

    @Override
    public String getPrincipalName() {
        return currentUserStorage.getCurrentUser().getLogin();
    }

    @Override
    public void signOut() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().navigate("");
    }
}
