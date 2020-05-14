package ru.artemov.victor.diploma.ui.users;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.web.context.annotation.SessionScope;
import ru.artemov.victor.diploma.domain.entities.operation.Operation;
import ru.artemov.victor.diploma.domain.entities.user.User;


@SessionScope
@SpringComponent
public class UserGrid extends Grid<User> {

    public UserGrid() {

        setSizeFull();

        addColumn(User::getFirstName).setHeader("Имя")
                .setFlexGrow(20)
                .setSortable(true)
                .setKey("firstName");

        addColumn(User::getSecondName)
                .setHeader("Фамилия")
                .setFlexGrow(20)
                .setSortable(true)
                .setKey("secondName");


        addColumn(User::getLogin)
                .setHeader("Логин")
                .setFlexGrow(12)
                .setKey("login");


        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(
                e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("firstName").setVisible(true);
            getColumnByKey("secondName").setVisible(true);
            getColumnByKey("login").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("firstName").setVisible(true);
            getColumnByKey("secondName").setVisible(true);
            getColumnByKey("login").setVisible(true);
        } else {
            getColumnByKey("firstName").setVisible(true);
            getColumnByKey("secondName").setVisible(true);
            getColumnByKey("login").setVisible(true);
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
