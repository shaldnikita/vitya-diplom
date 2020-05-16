package ru.artemov.victor.diploma.ui.users;

import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import ru.artemov.victor.diploma.domain.entities.operation.OperationType;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.OperationTypeRepository;
import ru.artemov.victor.diploma.ui.common.AbstractForm;

@SpringComponent
@Scope("prototype")
public class UserForm extends AbstractForm<User> {

    private final OperationTypeRepository operationTypeRepository;

    private  VerticalLayout content;

    private  TextField firstName;
    private  TextField secondName;
    private  TextField login;
    private  PasswordField password;
    private  MultiSelectListBox<OperationType> operationTypes;


    public UserForm(OperationTypeRepository operationTypeRepository) {
        super();
        this.operationTypeRepository = operationTypeRepository;

        setClassName("user-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("user-form-content");
        add(content);

        firstName = new TextField("Имя");
        firstName.setWidth("100%");
        firstName.setRequired(true);
        firstName.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(firstName);

        secondName = new TextField("Фамилия");
        secondName.setWidth("100%");
        secondName.setRequired(true);
        secondName.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(secondName);

        login = new TextField("Логин");
        login.setWidth("100%");
        login.setRequired(true);
        login.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(login);

        password = new PasswordField("Пароль");
        password.setWidth("100%");
        password.setRequired(true);
        password.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(password);

        operationTypes = new MultiSelectListBox<>();
        operationTypes.setWidth("100%");
        content.add(operationTypes);

        content.add(save, discard, delete, cancel);
    }

    @Override
    protected void initBinder() {
        binder = new BeanValidationBinder<>(User.class);
        binder.bindInstanceFields(this);
        binder.addStatusChangeListener(event -> {
            final boolean isValid = !event.hasValidationErrors();
            final boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });
    }

    public void refresh() {
        operationTypes.setItems(operationTypeRepository.findAll());
    }
}
