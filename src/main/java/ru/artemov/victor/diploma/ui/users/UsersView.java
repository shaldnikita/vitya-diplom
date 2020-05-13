package ru.artemov.victor.diploma.ui.users;

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
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;
import ru.artemov.victor.diploma.ui.MainLayout;

import javax.annotation.PostConstruct;


@Route(value = "Users", layout = MainLayout.class)
@SpringComponent
@UIScope
@RequiredArgsConstructor
public class UsersView extends HorizontalLayout
        implements HasUrlParameter<String> {

    private final UserRepository userRepository;
    private final UserViewLogic viewLogic;
    private final UserForm form;
    private DataProvider<User, Void> dataProvider;

    public static final String VIEW_NAME = "Пользователи";
    private UserGrid grid;
    private TextField filter;
    private Button newUser;


    @PostConstruct
    protected void init() {
        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    query.getLimit();
                    query.getOffset();
        return                    userRepository.findAll().stream();
                },
                query -> (int)userRepository.count()
                );
        viewLogic.setView(this);
        form.setViewLogic(viewLogic);

        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new UserGrid();
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

        newUser = new Button("Новый пользователь");
        // Setting theme variant of new production button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newUser.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newUser.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newUser.addClickListener(click -> viewLogic.newUser());
        // A shortcut to click the new product button by pressing ALT + N
        newUser.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newUser);
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
        newUser.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }


    public void selectRow(User row) {
        grid.getSelectionModel().select(row);
    }


    public void update(User user) {
       userRepository.save(user);
    dataProvider.refreshAll();
    }


    public void remove(User user) {
        userRepository.delete(user);
        dataProvider.refreshAll();
    }


    public void editProduct(User user) {
        form.refresh();
        showForm(user != null);
        form.editUser(user);
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
