package ru.artemov.victor.diploma.ui.animals;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;

/**
 * A form for editing a single product.
 */
public class AnimalForm extends Div {

    private final VerticalLayout content;

    private final TextField name;
    private final TextField uniqueIdentifier;
    private final Button delete;
    private final AnimalViewLogic viewLogic;
    private final Binder<Animal> binder;
    private Button save;
    private Button discard;
    private Button cancel;
    private Animal currentAnimal;


    public AnimalForm(AnimalViewLogic viewLogic) {
        setClassName("animal-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("animal-form-content");
        add(content);

        this.viewLogic = viewLogic;

        name = new TextField("Название");
        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(name);

        uniqueIdentifier = new TextField("Уникальный идентификатор");
        uniqueIdentifier.setWidth("100%");
        uniqueIdentifier.setRequired(true);
        uniqueIdentifier.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(uniqueIdentifier);
        
        binder = new BeanValidationBinder<>(Animal.class);
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
            if (currentAnimal != null
                    && binder.writeBeanIfValid(currentAnimal)) {
                this.viewLogic.saveProduct(currentAnimal);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Сбросить изменения");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> this.viewLogic.editProduct(currentAnimal));

        cancel = new Button("Отмена");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> this.viewLogic.cancelAnimal());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> this.viewLogic.cancelAnimal())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Удалить");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentAnimal != null) {
                this.viewLogic.deleteProduct(currentAnimal);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editAnimal(Animal animal) {
        if (animal == null) {
            animal = new Animal();
        }
        delete.setVisible(!animal.isNew());
        currentAnimal = animal;
        binder.readBean(animal);
    }
}
