package ru.artemov.victor.diploma.ui.common;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.Binder;
import lombok.Setter;
import ru.artemov.victor.diploma.domain.entities.AbstractEntity;

public abstract class AbstractForm<T extends AbstractEntity> extends Div {

    @Setter
    protected AbstractViewLogic<T> viewLogic;

    protected Binder<T> binder;

    protected final Button delete;
    protected final Button save;
    protected final Button discard;
    protected final Button cancel;
    private T currentItem;


    public AbstractForm() {
        getElement()
                .addEventListener("keydown", event -> this.viewLogic.cancel())
                .setFilter("event.key == 'Escape'");

        save = new Button("Сохранить");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentItem != null
                    && binder.writeBeanIfValid(currentItem)) {
                this.viewLogic.save(currentItem);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Сбросить изменения");
        discard.setWidth("100%");
        discard.addClickListener(
                event -> this.viewLogic.edit(currentItem));

        cancel = new Button("Отмена");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> this.viewLogic.cancel());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> this.viewLogic.cancel())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Удалить");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentItem != null) {
                this.viewLogic.delete(currentItem);
            }
        });

        initBinder();
        binder.addStatusChangeListener(event -> {
            final boolean isValid = !event.hasValidationErrors();
            final boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

    }

    protected abstract void initBinder();

    protected abstract void refresh();

    protected void editItem(T item) {
        delete.setVisible(!item.isNew());
        currentItem = item;
        binder.readBean(item);
    }
}
