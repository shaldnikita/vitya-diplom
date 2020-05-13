package ru.artemov.victor.diploma.ui.animals;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.AnimalRepository;
import ru.artemov.victor.diploma.ui.MainLayout;

import javax.annotation.PostConstruct;


@Route(value = "Animals", layout = MainLayout.class)
@SpringComponent
@UIScope
@RequiredArgsConstructor
public class AnimalsView extends HorizontalLayout
        implements HasUrlParameter<String> {

    private final AnimalRepository animalRepository;
    private final AnimalViewLogic viewLogic;
    private  DataProvider<Animal, Void> dataProvider;
    private AnimalForm form;

    public static final String VIEW_NAME = "Животные";
    private AnimalGrid grid;
    private TextField filter;
    private Button newAnimal;


    @PostConstruct
    protected void init() {
        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    query.getLimit();
                    query.getOffset();
        return                    animalRepository.findAll().stream();
                },
                query -> (int) animalRepository.count()
                );
        viewLogic.setView(this);
        form = new AnimalForm(viewLogic);

        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new AnimalGrid();
        grid.setDataProvider(dataProvider);
        // Allows user to select a single row in the grid.
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

        final VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Фильтр по названию");
        // Apply the filter to grid's data provider. TextField value is never
       /* filter.addValueChangeListener(
                event -> dataProvider.setFilter(event.getValue()));*/
        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newAnimal = new Button("Новое живтоное");
        // Setting theme variant of new production button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newAnimal.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newAnimal.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newAnimal.addClickListener(click -> viewLogic.newAnimal());
        // A shortcut to click the new product button by pressing ALT + N
        newAnimal.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newAnimal);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showError(String msg) {
        Notification.show(msg);
    }


    public void showNotification(String msg) {
        Notification.show(msg);
    }


    public void setNewUserEnabled(boolean enabled) {
        newAnimal.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }


    public void selectRow(Animal row) {
        grid.getSelectionModel().select(row);
    }


    public void update(Animal animal) {
       animalRepository.save(animal);
    dataProvider.refreshAll();
    }


    public void remove(Animal animal) {
        animalRepository.delete(animal);
        dataProvider.refreshAll();
    }


    public void editProduct(Animal animal) {
        showForm(animal != null);
        form.editAnimal(animal);
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
}
