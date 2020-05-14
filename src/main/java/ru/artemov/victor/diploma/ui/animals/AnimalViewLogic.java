package ru.artemov.victor.diploma.ui.animals;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.ui.common.AbstractViewLogic;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the product editor form and the data source, including
 * fetching and saving products.
 *
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
@UIScope
@SpringComponent
public class AnimalViewLogic extends AbstractViewLogic<Animal> {

    public AnimalViewLogic(JpaRepository<Animal, Integer> repository) {
        super(repository);
    }

    @Override
    protected Animal getNewItem() {
        return new Animal();
    }
}
