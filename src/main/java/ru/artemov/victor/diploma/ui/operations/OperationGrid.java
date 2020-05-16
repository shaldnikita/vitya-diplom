package ru.artemov.victor.diploma.ui.operations;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;


@SpringComponent
@Scope("prototype")
public class OperationGrid extends Grid<Operation> {

    public OperationGrid() {

        setSizeFull();

        addColumn(Operation::getName).setHeader("Название операции")
                .setFlexGrow(20).setSortable(true).setKey("name");


        // Add an traffic light icon in front of availability
        // Three css classes with the same names of three availability values,
        // Available, Coming and Discontinued, are defined in shared-styles.css
        // and are
        // used here in availabilityTemplate.
       /* final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.availability]]\"></iron-icon> [[item.availability]]";
        addColumn(TemplateRenderer.<Operation>of(availabilityTemplate)
                .withProperty("availability",
                        product -> product.getAvailability().toString()))
                                .setHeader("Availability")
                                .setComparator(Comparator
                                        .comparing(Operation::getAvailability))
                                .setFlexGrow(5).setKey("availability");*/


        addColumn(Operation::getType).setHeader("Тип операции").setFlexGrow(12)
                .setKey("type");

        addColumn(Operation::getAnimal).setHeader("Животное").setFlexGrow(12)
                .setKey("animal");


        addColumn(Operation::getAuthor).setHeader("Ответственный")
                .setFlexGrow(12)
                .setKey("author");

        addColumn(Operation::getDate).setHeader("Дата")
                .setFlexGrow(12)
                .setKey("date");


        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(
                e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("type").setVisible(true);
            getColumnByKey("author").setVisible(true);
            getColumnByKey("date").setVisible(true);
            getColumnByKey("name").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("type").setVisible(true);
            getColumnByKey("author").setVisible(true);
            getColumnByKey("date").setVisible(true);
            getColumnByKey("name").setVisible(true);
        } else {
            getColumnByKey("type").setVisible(true);
            getColumnByKey("author").setVisible(true);
            getColumnByKey("date").setVisible(true);
            getColumnByKey("name").setVisible(true);
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
