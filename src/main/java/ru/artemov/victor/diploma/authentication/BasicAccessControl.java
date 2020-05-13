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
            CurrentUser.set(maybeUser.get().getLogin());
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean isUserSignedIn() {
        return !CurrentUser.get().isEmpty();
    }

    @Override
    public boolean isUserInRole(String role) {
        if ("admin".equals(role)) {
            // Only the "admin" user is in the "admin" role
            return getPrincipalName().equals("admin");
        }

        // All users are in all non-admin roles
        return true;
    }

    @Override
    public String getPrincipalName() {
        return CurrentUser.get();
    }

    @Override
    public void signOut() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().navigate("");
    }
}
