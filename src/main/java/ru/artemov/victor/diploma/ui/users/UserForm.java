package ru.artemov.victor.diploma.ui.users;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.artemov.victor.diploma.authentication.CurrentUser;
import ru.artemov.victor.diploma.domain.entities.operation.OperationType;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.OperationTypeRepository;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
@RequiredArgsConstructor
public class UserForm extends Div {

    private final OperationTypeRepository operationTypeRepository;
    private final UserRepository userRepository;

    private  VerticalLayout content;

    private  TextField firstName;
    private  TextField secondName;
    private  TextField login;
    private  PasswordField password;
    private  MultiSelectListBox<OperationType> operationTypes;
    private  Button delete;
    @Setter
    private  UserViewLogic viewLogic;
    private  Binder<User> binder;
    private Button save;
    private Button discard;
    private Button cancel;
    private User currentUser;


    @PostConstruct
    public void init() {
        setClassName("user-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("user-form-content");
        add(content);

        this.viewLogic = viewLogic;

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

        binder = new BeanValidationBinder<>(User.class);
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
            if (currentUser != null
                    && binder.writeBeanIfValid(currentUser)) {
                this.viewLogic.saveProduct(currentUser);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Сбросить изменения");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> this.viewLogic.editProduct(currentUser));

        cancel = new Button("Отмена");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> this.viewLogic.cancelUser());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> this.viewLogic.cancelUser())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Удалить");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentUser != null) {
                this.viewLogic.deleteProduct(currentUser);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editUser(User user) {
        if (user == null) {
            user = new User();
        }
        delete.setVisible(!user.isNew());
        currentUser = user;
        binder.readBean(user);
    }

    public void refresh() {
        /*var allowedTypes = userRepository.findByLogin(CurrentUser.get()).get().getOperationTypes();
        operationTypes.setItems(operationTypeRepository.findAll().stream().filter(allowedTypes::contains));*/
        operationTypes.setItems(operationTypeRepository.findAll());
    }
}
