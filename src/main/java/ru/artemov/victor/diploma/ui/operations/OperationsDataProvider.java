package ru.artemov.victor.diploma.ui.operations;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.web.context.annotation.SessionScope;
import ru.artemov.victor.diploma.authentication.CurrentUserStorage;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.repositories.OperationRepository;

@SpringComponent
@SessionScope
public class OperationsDataProvider extends CallbackDataProvider<Operation, Void> {

    public OperationsDataProvider(OperationRepository operationRepository, CurrentUserStorage currentUserStorage) {
        super( query -> {
                    query.getLimit();
                    query.getOffset();
                    var allowedTypes = currentUserStorage.getFreshCurrentUser().getOperationTypes();

                    return operationRepository.findAll().stream().filter(op -> allowedTypes.contains(op.getType()));
                },
                query -> (int) operationRepository.count());
    }

}
