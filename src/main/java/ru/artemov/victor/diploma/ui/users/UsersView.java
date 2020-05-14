package ru.artemov.victor.diploma.ui.users;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;
import ru.artemov.victor.diploma.ui.MainLayout;
import ru.artemov.victor.diploma.ui.common.GridView;


@Route(value = "Users", layout = MainLayout.class)
@SpringComponent
@UIScope
@Lazy
public class UsersView extends GridView<User> {


    public UsersView(UsersDataProvider dataProvider,
                     UserViewLogic viewLogic,
                     UserGrid grid,
                     UserForm form,
                     UserRepository repository) {
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
