package ru.artemov.victor.diploma.ui.operations;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import ru.artemov.victor.diploma.authentication.CurrentUser;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.repositories.OperationRepository;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;
import ru.artemov.victor.diploma.ui.MainLayout;

import javax.annotation.PostConstruct;


@Route(value = "Operations", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@SpringComponent
@UIScope
@RequiredArgsConstructor
public class OperationsView extends HorizontalLayout
        implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Технологические операции";
    private final OperationRepository operationRepository;
    private final OperationViewLogic viewLogic;
    private final UserRepository userRepository;
    private final OperationForm form;
    private DataProvider<Operation, Void> dataProvider;
    private OperationGrid grid;
    private TextField filter;
    private Button newOperation;


    @PostConstruct
    protected void init() {
        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    query.getLimit();
                    query.getOffset();
                    var allowedTypes = userRepository.findByLogin(CurrentUser.get()).get().getOperationTypes();

                    return operationRepository.findAll().stream().filter(op -> allowedTypes.contains(op.getType()));
                },
                query -> (int) operationRepository.count()
        );
        viewLogic.setView(this);
        form.setViewLogic(viewLogic);

        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new OperationGrid();
        grid.setDataProvider(dataProvider);
        // Allows user to select a single row in the grid.
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

        final VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Фильтр по названию");
        // Apply the filter to grid's data provider. TextField value is never
       /* filter.addValueChangeListener(
                event -> dataProvider.setFilter(event.getValue()));*/
        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newOperation = new Button("Новая операция");
        // Setting theme variant of new production button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newOperation.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newOperation.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newOperation.addClickListener(click -> viewLogic.newOperation());
        // A shortcut to click the new product button by pressing ALT + N
        newOperation.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newOperation);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showError(String msg) {
        Notification.show(msg);
    }


    public void showNotification(String msg) {
        Notification.show(msg);
    }


    public void setNewOperationEnabled(boolean enabled) {
        newOperation.setEnabled(enabled);
    }


    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }


    public void selectRow(Operation row) {
        grid.getSelectionModel().select(row);
    }


    public void updateProduct(Operation operation) {
        operation.getAuthor().getOperations().add(operation);
        operationRepository.save(operation);
        dataProvider.refreshAll();
    }


    public void removeProduct(Operation operation) {
        operationRepository.delete(operation);
        dataProvider.refreshAll();
    }


    public void editProduct(Operation operation) {
        form.refresh();
        showForm(operation != null);
        form.editProduct(operation);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}
