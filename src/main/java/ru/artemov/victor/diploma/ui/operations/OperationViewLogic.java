package ru.artemov.victor.diploma.ui.operations;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemov.victor.diploma.authentication.CurrentUserStorage;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.ui.common.AbstractViewLogic;

@UIScope
@SpringComponent
public class OperationViewLogic extends AbstractViewLogic<Operation> {

    private final CurrentUserStorage currentUserStorage;

    public OperationViewLogic(JpaRepository<Operation, Integer> repository, CurrentUserStorage currentUserStorage) {
        super(repository);
        this.currentUserStorage = currentUserStorage;
    }

    @Override
    protected Operation getNewItem() {
        return new Operation(currentUserStorage.getCurrentUser());
    }
}
