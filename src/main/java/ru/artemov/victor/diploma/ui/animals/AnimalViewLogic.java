package ru.artemov.victor.diploma.ui.animals;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.artemov.victor.diploma.authentication.AccessControl;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.domain.repositories.AnimalRepository;

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
public class AnimalViewLogic implements Serializable {

    @Setter
    private AnimalsView view;
    private final AnimalRepository animalRepository;
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

    public void cancelAnimal() {
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

        UI.getCurrent().navigate(AnimalsView.class, fragmentParameter);
    }


    public void enter(String animalId) {
        if (animalId != null && !animalId.isEmpty()) {
            if (animalId.equals("new")) {
                newAnimal();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final int aid = Integer.parseInt(animalId);
                    final Animal animal = findAnimal(aid);
                    view.selectRow(animal);
                } catch (final NumberFormatException ignored) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Animal findAnimal(int animalId) {
        return animalRepository.findById(animalId).get();
    }

    public void saveProduct(Animal animal) {
        final boolean newAnimal = animal.isNew();
        view.clearSelection();
        view.update(animal);
        setFragmentParameter("");
        view.showNotification(animal.getUniqueIdentifier()
                + (newAnimal ? " created" : " updated"));
    }

    public void deleteProduct(Animal animal) {
        view.clearSelection();
        view.remove(animal);
        setFragmentParameter("");
        view.showNotification(animal.getUniqueIdentifier() + " removed");
    }

    public void editProduct(Animal animal) {
        if (animal == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(animal.getId() + "");
        }
        view.editProduct(animal);
    }

    public void newAnimal() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editProduct(new Animal());
    }

    public void rowSelected(Animal animal) {
        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editProduct(animal);
        }
    }
}
