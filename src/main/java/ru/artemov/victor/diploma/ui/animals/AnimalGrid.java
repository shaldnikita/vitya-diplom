package ru.artemov.victor.diploma.ui.animals;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;

@SpringComponent
@Scope("prototype")
public class AnimalGrid extends Grid<Animal> {

    public AnimalGrid() {

        setSizeFull();

        addColumn(Animal::getName).setHeader("Название")
                .setFlexGrow(20)
                .setSortable(true)
                .setKey("name");

        addColumn(Animal::getUniqueIdentifier)
                .setHeader("Уникальный идентификатор")
                .setFlexGrow(20)
                .setSortable(true)
                .setKey("uniqueIdentifier");

        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(
                e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("name").setVisible(true);
            getColumnByKey("uniqueIdentifier").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("name").setVisible(true);
            getColumnByKey("uniqueIdentifier").setVisible(true);
        } else {
            getColumnByKey("name").setVisible(true);
            getColumnByKey("uniqueIdentifier").setVisible(true);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // fetch browser width
        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> {
            setColumnVisibility(e.getBodyClientWidth());
        });
    }

}
