package ru.artemov.victor.diploma.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import ru.artemov.victor.diploma.authentication.AccessControl;
import ru.artemov.victor.diploma.ui.login.LoginScreen;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

/**
 * View shown when trying to navigate to a view that does not exist using
 */
@ParentLayout(MainLayout.class)
@SpringComponent
@UIScope
@RequiredArgsConstructor
public class ErrorView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    private final AccessControl accessControl;

    private Span explanation;

    @PostConstruct
    public void init() {
        H1 header = new H1("The view could not be found.");
        add(header);

        explanation = new Span();
        add(explanation);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        if (!accessControl.isUserSignedIn()) {
            event.rerouteTo(LoginScreen.class);
            return HttpServletResponse.SC_FORBIDDEN;
        }
        explanation.setText("Could not navigate to '"
                + event.getLocation().getPath() + "'.");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
