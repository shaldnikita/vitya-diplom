package ru.artemov.victor.diploma.ui.animals;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.ui.common.AbstractForm;

@SpringComponent
@Scope("prototype")
public class AnimalForm extends AbstractForm<Animal> {

    private final VerticalLayout content;

    private final TextField uniqueIdentifier;
    private final TextField name;
    private final DatePicker birthday;
    private final DatePicker coming;
    private final DatePicker lastWeightDate;
    private final NumberField weight;


    public AnimalForm() {
        super();
        setClassName("animal-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("animal-form-content");
        add(content);

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

        birthday = new DatePicker("Дата рождения");
        birthday.setWidth("100%");
        birthday.setRequired(true);
        content.add(birthday);

        coming = new DatePicker("Дата послупления");
        coming.setWidth("100%");
        coming.setRequired(true);
        content.add(coming);

        lastWeightDate = new DatePicker("Дата последнего взвешивания");
        lastWeightDate.setWidth("100%");
        lastWeightDate.setRequired(true);
        content.add(lastWeightDate);

        weight = new NumberField("Вес");
        weight.setWidth("100%");
        weight.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(weight);

        content.add(save, discard, delete, cancel);
    }

    @Override
    protected void initBinder() {
        binder = new BeanValidationBinder<>(Animal.class);
        binder.bindInstanceFields(this);

    }

    @Override
    protected void refresh() {
        //do nothing
    }
}
