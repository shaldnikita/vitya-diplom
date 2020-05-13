package ru.artemov.victor.diploma.ui.operations;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.artemov.victor.diploma.authentication.AccessControl;
import ru.artemov.victor.diploma.authentication.CurrentUser;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.repositories.OperationRepository;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;

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
public class OperationViewLogic implements Serializable {

    @Setter
    private OperationsView view;
    private final OperationRepository operationRepository;
    private final UserRepository userRepository;
    private final AccessControl accessControl;

    /**
     * Does the initialization of the inventory view including disabling the
     * buttons if the user doesn't have access.
     */
    public void init() {
        if (!accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewOperationEnabled(false);
        }
    }

    public void cancelProduct() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Updates the fragment without causing OperationViewLogic navigator to
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

        UI.getCurrent().navigate(OperationsView.class, fragmentParameter);
    }

    /**
     * Opens the product form and clears its fields to make it ready for
     * entering a new product if operationId is null, otherwise loads the product
     * with the given operationId and shows its data in the form fields so the
     * user can edit them.
     *
     * 
     * @param operationId
     */
    public void enter(String operationId) {
        if (operationId != null && !operationId.isEmpty()) {
            if (operationId.equals("new")) {
                newOperation();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final int pid = Integer.parseInt(operationId);
                    final Operation operation = findProduct(pid);
                    view.selectRow(operation);
                } catch (final NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Operation findProduct(int operationId) {
        return operationRepository.findById(operationId).get();
    }

    public void saveProduct(Operation operation) {
        final boolean newProduct = operation.isNew();
        view.clearSelection();
        view.updateProduct(operation);
        setFragmentParameter("");
        view.showNotification(operation.getName()
                + (newProduct ? " created" : " updated"));
    }

    public void deleteProduct(Operation operation) {
        view.clearSelection();
        view.removeProduct(operation);
        setFragmentParameter("");
        view.showNotification(operation.getName() + " removed");
    }

    public void editProduct(Operation operation) {
        if (operation == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(operation.getId() + "");
        }
        view.editProduct(operation);
    }

    public void newOperation() {
        view.clearSelection();
        setFragmentParameter("new");
        var operation = new Operation();
        userRepository.findByLogin(CurrentUser.get()).ifPresent(operation::setAuthor);
        view.editProduct(operation);
    }

    public void rowSelected(Operation operation) {
        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editProduct(operation);
        }
    }
}
