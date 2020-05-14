package ru.artemov.victor.diploma.ui.common;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.OptionalParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemov.victor.diploma.domain.entities.AbstractEntity;

public abstract class GridView<T extends AbstractEntity> extends AbstractView<T> {

    protected Grid<T> grid;

    protected AbstractForm<T> form;

    protected TextField filter;

    protected Button newItem;

    private JpaRepository<T, Integer> repository;


    public GridView(DataProvider<T, Void> dataProvider, AbstractViewLogic<T> viewLogic, Grid<T> grid, AbstractForm<T> form, JpaRepository<T, Integer> repository) {
        super(dataProvider, viewLogic);
        viewLogic.setView(this);
        form.setViewLogic(viewLogic);
        this.grid = grid;
        this.form = form;
        this.repository = repository;

        grid.setDataProvider(this.dataProvider);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

        viewLogic.setView(this);
        form.setViewLogic(viewLogic);

        newItem = new Button("Новая запись");
        newItem.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newItem.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newItem.addClickListener(click -> viewLogic.newItem());
        newItem.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        filter = new TextField();
        filter.setPlaceholder("Фильтр по названию");

        // Apply the filter to grid's data provider. TextField value is never
        /* filter.addValueChangeListener(
                event -> dataProvider.setFilter(event.getValue()));*/

        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
    }

    public void showNotification(String msg) {
        Notification.show(msg);
    }

    public void setNewItemEnabled(boolean enabled) {
        newItem.setEnabled(enabled);
    }

    public void update(T item) {
        repository.save(item);
        dataProvider.refreshAll();
    }


    public void remove(T item) {
        repository.delete(item);
        dataProvider.refreshAll();
    }


    public void edit(T item) {
        form.refresh();
        showForm(item != null);
        form.editItem(item);
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

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }


    public void selectRow(T row) {
        grid.getSelectionModel().select(row);
    }

    public HorizontalLayout createTopBar() {
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newItem);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }
}
