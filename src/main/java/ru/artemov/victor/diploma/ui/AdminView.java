package ru.artemov.victor.diploma.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.ironlist.IronList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import ru.artemov.victor.diploma.domain.entities.operation.OperationType;
import ru.artemov.victor.diploma.domain.repositories.OperationTypeRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

/**
 * Admin view that is registered dynamically on admin user login.
 * <p>
 * Allows CRUD operations for the book categories.
 */
@RequiredArgsConstructor
@Route(value = "Categories", layout = MainLayout.class)
public class AdminView extends VerticalLayout {

    public static final String VIEW_NAME = "Администратор";

    private final OperationTypeRepository operationTypeRepository;

    private final IronList<OperationType>  categoriesListing = new IronList<>();;
    private ListDataProvider<OperationType> dataProvider;

    private Button newCategoryButton ;

    @PostConstruct
    public void init() {


        dataProvider = new ListDataProvider<OperationType>(
                new ArrayList<>(operationTypeRepository.findAll()));

        newCategoryButton = new Button("Добавить новую категорию", event -> {
            final OperationType operationType = new OperationType();
            dataProvider.getItems().add(operationType);
            dataProvider.refreshAll();
        });

        categoriesListing.setDataProvider(dataProvider);
        categoriesListing.setRenderer(
                new ComponentRenderer<>(this::createCategoryEditor));


        newCategoryButton.setDisableOnClick(true);

        add(new H2("Добрый день, Виктор!"), new H4("Редактировать категорию"), newCategoryButton,
                categoriesListing);
    }

    private Component createCategoryEditor(OperationType operationType) {
        final TextField nameField = new TextField();
        if (operationType.getId() < 0) {
            nameField.focus();
        }



        final BeanValidationBinder<OperationType> binder = new BeanValidationBinder<>(
                OperationType.class);
        binder.forField(nameField).bind("name");
        binder.setBean(operationType);
        binder.addValueChangeListener(event -> {
            if (binder.isValid()) {
                operationTypeRepository.save(operationType);
                newCategoryButton.setEnabled(true);
                Notification.show("Категория сохранена.");
            }
        });

        final HorizontalLayout layout = new HorizontalLayout(nameField);
        layout.setFlexGrow(1);
        return layout;
    }

}
