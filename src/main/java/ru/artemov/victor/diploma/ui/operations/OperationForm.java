package ru.artemov.victor.diploma.ui.operations;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import ru.artemov.victor.diploma.authentication.CurrentUserStorage;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.entities.operation.OperationType;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.AnimalRepository;
import ru.artemov.victor.diploma.domain.repositories.OperationTypeRepository;
import ru.artemov.victor.diploma.ui.common.AbstractForm;

import java.util.List;

@SpringComponent
@Scope("prototype")
public class OperationForm extends AbstractForm<Operation> {

    private final AnimalRepository animalRepository;
    private final OperationTypeRepository operationTypeRepository;
    private final CurrentUserStorage currentUserStorage;

    private  VerticalLayout content;
    private  TextField name;
    private  ComboBox<User> author;
    private  DatePicker date;
    private  ComboBox<OperationType> type;
    private  ComboBox<Animal> animal;


    public OperationForm(AnimalRepository animalRepository, OperationTypeRepository operationTypeRepository,  CurrentUserStorage currentUserStorage) {
        super();
        this.animalRepository = animalRepository;
        this.operationTypeRepository = operationTypeRepository;
        this.currentUserStorage = currentUserStorage;

        setClassName("operation-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("operation-form-content");
        add(content);

        name = new TextField("Название операции");
        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(name);

        author = new ComboBox<>("Ответственный");

        date = new DatePicker("Дата создания");

        final HorizontalLayout horizontalLayout = new HorizontalLayout(author,
                date);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setFlexGrow(1, author, date);
        content.add(horizontalLayout);

        type = new ComboBox();
        type.setLabel("Категория");
        type.setId("type");
        content.add(type);

        animal = new ComboBox();
        animal.setLabel("Животное");
        animal.setId("animal");
        content.add(animal);


        content.add(save, discard, delete, cancel);
        refresh();
    }

    @Override
    protected void initBinder() {
        binder = new BeanValidationBinder<>(Operation.class);
        binder.bindInstanceFields(this);
    }

    @Override
    protected void refresh() {
        var currentUser =  currentUserStorage.getFreshCurrentUser();
        var allowedTypes = currentUser.getOperationTypes();
        type.setItems(operationTypeRepository.findAll().stream().filter(allowedTypes::contains));
        animal.setItems(animalRepository.findAll());
        author.setItems(List.of(currentUser));
    }
}
