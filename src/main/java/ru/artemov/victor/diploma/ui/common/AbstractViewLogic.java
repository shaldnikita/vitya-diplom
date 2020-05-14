package ru.artemov.victor.diploma.ui.common;


import com.vaadin.flow.component.UI;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemov.victor.diploma.domain.entities.AbstractEntity;
import ru.artemov.victor.diploma.ui.operations.OperationsView;

public abstract class AbstractViewLogic<T extends AbstractEntity> {

    @Setter
    protected GridView<T> view;

    protected final JpaRepository<T, Integer> repository;

    protected abstract T getNewItem();


    public AbstractViewLogic(JpaRepository<T, Integer> repository) {
        this.repository = repository;
    }

    public void enter(String id) {
        if (id != null && !id.isEmpty()) {
            if (id.equals("new")) {
                newItem();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final int intId = Integer.parseInt(id);
                    repository.findById(intId).ifPresent(item -> view.selectRow(item));
                } catch (final NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    public void cancel() {
        setFragmentParameter("");
        view.clearSelection();
    }

    private void setFragmentParameter(String operationId) {
        String fragmentParameter;
        if (operationId == null || operationId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = operationId;
        }

        UI.getCurrent().navigate(OperationsView.class, fragmentParameter);
    }

    public void save(T item) {
        final boolean newProduct = item.isNew();
        view.clearSelection();
        view.update(item);
        setFragmentParameter("");
        view.showNotification( "Запись "
                + (newProduct ? "создана" : "обновлена"));
    }

    public void delete(T item) {
        view.clearSelection();
        view.remove(item);
        setFragmentParameter("");
        view.showNotification("Запись");
    }

    public void edit(T item) {
        if (item == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(item.getId() + "");
        }
        view.edit(item);
    }

    public void newItem() {
        view.clearSelection();
        setFragmentParameter("new");
        view.edit(getNewItem());
    }

    public void rowSelected(T item) {
        edit(item);
    }
}
