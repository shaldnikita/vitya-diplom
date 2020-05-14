package ru.artemov.victor.diploma.ui.animals;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Lazy;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.domain.repositories.AnimalRepository;
import ru.artemov.victor.diploma.ui.MainLayout;
import ru.artemov.victor.diploma.ui.common.GridView;


@Route(value = "Animals", layout = MainLayout.class)
@SpringComponent
@UIScope
@Lazy
public class AnimalsView extends GridView<Animal> {

    public AnimalsView(AnimalRepository repository,
                       AnimalViewLogic viewLogic,
                       AnimalsDataProvider dataProvider,
                       AnimalGrid grid,
                       AnimalForm form) {
        super(dataProvider, viewLogic, grid, form, repository);
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        final VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);
    }
}
