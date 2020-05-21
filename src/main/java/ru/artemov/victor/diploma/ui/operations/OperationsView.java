package ru.artemov.victor.diploma.ui.operations;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import ru.artemov.victor.diploma.authentication.CurrentUserStorage;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.repositories.OperationRepository;
import ru.artemov.victor.diploma.ui.MainLayout;
import ru.artemov.victor.diploma.ui.common.GridView;


@Route(value = "Operations", layout = MainLayout.class)
public class OperationsView extends GridView<Operation> {

    private final CurrentUserStorage currentUserStorage;
    private final OperationRepository operationRepository;

    public OperationsView(OperationViewLogic viewLogic, OperationGrid grid,
                          OperationForm form,
                          OperationRepository repository,
                          OperationsDataProvider dataProvider,
                          CurrentUserStorage currentUserStorage,
                          OperationRepository operationRepository) {
        super(dataProvider, viewLogic, grid, form, repository);
        this.currentUserStorage = currentUserStorage;
        this.operationRepository = operationRepository;
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        final VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);
        add(barAndGridLayout);
        add(form);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        fillGrid();
    }

    @Override
    protected void fillGrid() {
        var allowedTypes = currentUserStorage.getFreshCurrentUser().getOperationTypes();
        grid.setItems(operationRepository.findAll().stream().filter(op -> allowedTypes.contains(op.getType())));
    }
}
