package ru.artemov.victor.diploma.ui.operations;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;
import ru.artemov.victor.diploma.authentication.CurrentUser;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.entities.operation.OperationType;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.AnimalRepository;
import ru.artemov.victor.diploma.domain.repositories.OperationTypeRepository;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * A form for editing a single product.
 */
@SessionScope
@SpringComponent
@RequiredArgsConstructor
public class OperationForm extends Div {

    private final AnimalRepository animalRepository;
    private final OperationTypeRepository operationTypeRepository;
    private final UserRepository userRepository;

    private  VerticalLayout content;

    private  TextField operationName;
    private  ComboBox<User> author;
    private  DatePicker createdDate;
    private  ComboBox<OperationType> type;
    private  ComboBox<Animal> animal;
    private  Button delete;
    @Setter
    private  OperationViewLogic viewLogic;
    private  Binder<Operation> binder;
    private Button save;
    private Button discard;
    private Button cancel;
    private Operation currentTechnologicalOperation;


    @PostConstruct
    public void init() {
        setClassName("operation-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("operation-form-content");
        add(content);

        operationName = new TextField("Название операции");
        operationName.setWidth("100%");
        operationName.setRequired(true);
        operationName.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(operationName);

        author = new ComboBox<>("Ответственный");

        createdDate = new DatePicker("Дата создания");

        final HorizontalLayout horizontalLayout = new HorizontalLayout(author,
                createdDate);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setFlexGrow(1, author, createdDate);
        content.add(horizontalLayout);

        type = new ComboBox();
        type.setLabel("Категория");
        type.setId("type");
        content.add(type);

        animal = new ComboBox();
        animal.setLabel("Животное");
        animal.setId("animal");
        content.add(animal);

        binder = new BeanValidationBinder<>(Operation.class);
        binder.forField(author).bind("author");
        binder.forField(operationName).bind("name");
        binder.forField(createdDate).bind("date");
        binder.forField(type).bind("type");
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            final boolean isValid = !event.hasValidationErrors();
            final boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save = new Button("Сохранить");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentTechnologicalOperation != null
                    && binder.writeBeanIfValid(currentTechnologicalOperation)) {
                this.viewLogic.saveProduct(currentTechnologicalOperation);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Сбросить изменения");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> this.viewLogic.editProduct(currentTechnologicalOperation));

        cancel = new Button("Отмена");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> this.viewLogic.cancelProduct());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> this.viewLogic.cancelProduct())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Удалить");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentTechnologicalOperation != null) {
                this.viewLogic.deleteProduct(currentTechnologicalOperation);
            }
        });

        content.add(save, discard, delete, cancel);
        refresh();
    }

    public void editProduct(Operation operation) {
        if (operation == null) {
            operation = new Operation();
        }
        author.setItems(List.of(operation.getAuthor()));
        delete.setVisible(!operation.isNew());
        currentTechnologicalOperation = operation;
        author.setReadOnly(false);
        binder.readBean(operation);
        author.setReadOnly(true);
    }

    public void refresh() {
        var allowedTypes = userRepository.findByLogin(CurrentUser.get()).map(User::getOperationTypes);
        type.setItems(operationTypeRepository.findAll().stream().filter(op ->
             allowedTypes.map(operationTypes -> operationTypes.contains(op)).orElse(false)
        ));
        animal.setItems(animalRepository.findAll());
    }
}
