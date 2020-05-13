package ru.artemov.victor.diploma.ui.users;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.artemov.victor.diploma.authentication.AccessControl;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.OperationRepository;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;
import ru.artemov.victor.diploma.ui.operations.OperationsView;

import java.io.Serializable;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the product editor form and the data source, including
 * fetching and saving products.
 *
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
@RequiredArgsConstructor
@UIScope
@SpringComponent
public class UserViewLogic implements Serializable {

    @Setter
    private UsersView view;
    private final OperationRepository operationRepository;
    private final UserRepository userRepository;
    private final AccessControl accessControl;

    /**
     * Does the initialization of the inventory view including disabling the
     * buttons if the user doesn't have access.
     */
    public void init() {
        if (!accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewUserEnabled(false);
        }
    }

    public void cancelUser() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Updates the fragment without causing UserViewLogic navigator to
     * change view. It actually appends the operationId as a parameter to the URL.
     * The parameter is set to keep the view state the same during e.g. a
     * refresh and to enable bookmarking of individual product selections.
     *
     */
    private void setFragmentParameter(String operationId) {
        String fragmentParameter;
        if (operationId == null || operationId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = operationId;
        }

        UI.getCurrent().navigate(UsersView.class, fragmentParameter);
    }


    public void enter(String userId) {
        if (userId != null && !userId.isEmpty()) {
            if (userId.equals("new")) {
                newUser();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final int uid = Integer.parseInt(userId);
                    final User user = findUser(uid);
                    view.selectRow(user);
                } catch (final NumberFormatException ignored) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private User findUser(int userId) {
        return userRepository.findById(userId).get();
    }

    public void saveProduct(User user) {
        final boolean newUser = user.isNew();
        view.clearSelection();
        view.update(user);
        setFragmentParameter("");
        view.showNotification(user.getLogin()
                + (newUser ? " created" : " updated"));
    }

    public void deleteProduct(User user) {
        view.clearSelection();
        view.remove(user);
        setFragmentParameter("");
        view.showNotification(user.getLogin() + " removed");
    }

    public void editProduct(User user) {
        if (user == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(user.getId() + "");
        }
        view.editProduct(user);
    }

    public void newUser() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editProduct(new User());
    }

    public void rowSelected(User user) {
        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editProduct(user);
        }
    }
}
