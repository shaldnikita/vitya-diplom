package ru.artemov.victor.diploma.ui.operations;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.repositories.OperationRepository;
import ru.artemov.victor.diploma.ui.MainLayout;
import ru.artemov.victor.diploma.ui.common.GridView;


@Route(value = "Operations", layout = MainLayout.class)
public class OperationsView extends GridView<Operation> {


    public OperationsView(OperationViewLogic viewLogic, OperationGrid grid,
                          OperationForm form,
                          OperationRepository repository,
                          OperationsDataProvider dataProvider) {
        super(dataProvider, viewLogic, grid, form, repository);

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

}
