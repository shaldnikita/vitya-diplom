package ru.artemov.victor.diploma.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.RequiredArgsConstructor;
import ru.artemov.victor.diploma.authentication.AccessControl;
import ru.artemov.victor.diploma.ui.animals.AnimalsView;
import ru.artemov.victor.diploma.ui.operations.OperationsView;
import ru.artemov.victor.diploma.ui.users.UsersView;

import javax.annotation.PostConstruct;

/**
 * The main layout. Contains the navigation menu.
 */
@Theme(value = Lumo.class)
@PWA(name = "Учет технологических операций", shortName = "Учет ТУ", enableInstallPrompt = false)
@CssImport("styles/shared-styles.css")
@CssImport(value = "styles/menu-buttons.css", themeFor = "vaadin-button")
@Route
@RequiredArgsConstructor
public class MainLayout extends AppLayout implements RouterLayout {

    private final AccessControl accessControl;

    private Button logoutButton;


    @PostConstruct
    public void init() {

        // Header of the menu (the navbar)

        // menu toggle
        final DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassName("menu-toggle");
        addToNavbar(drawerToggle);

        // image, logo
        final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        top.setClassName("menu-header");

        // Note! Image resource url is resolved here as it is dependent on the
        // execution mode (development or production) and browser ES level
        // support
        final String resolvedImage = VaadinService.getCurrent().resolveResource(
                "img/table-logo.png", VaadinSession.getCurrent().getBrowser());

        final Image image = new Image(resolvedImage, "");
        final Label title = new Label("Учет ТО");
        top.add(image, title);
        top.add(title);
        addToNavbar(top);

        // Navigation items
        addToDrawer(createMenuLink(OperationsView.class, "Технологические операции",
                VaadinIcon.EDIT.create()));

        // Navigation items
        addToDrawer(createMenuLink(AnimalsView.class, "Животные",
                VaadinIcon.TWITTER.create()));

        // Create logout button but don't add it yet; admin view might be added
        // in between (see #onAttach())
        logoutButton = createMenuButton("Выход", VaadinIcon.SIGN_OUT.create());
        logoutButton.addClickListener(e -> logout());
        logoutButton.getElement().setAttribute("title", "Выход (Ctrl+L)");

    }

    private void logout() {
        accessControl.signOut();
    }

    private RouterLink createMenuLink(Class<? extends Component> viewClass,
            String caption, Icon icon) {
        final RouterLink routerLink = new RouterLink(null, viewClass);
        routerLink.setClassName("menu-link");
        routerLink.add(icon);
        routerLink.add(new Span(caption));
        icon.setSize("24px");
        return routerLink;
    }

    private Button createMenuButton(String caption, Icon icon) {
        final Button routerButton = new Button(caption);
        routerButton.setClassName("menu-button");
        routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        routerButton.setIcon(icon);
        icon.setSize("24px");
        return routerButton;
    }

    private void registerAdminViewsIfApplicable(AccessControl accessControl) {
        // register the admin view dynamically only for any admin user logged in
        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            if(!RouteConfiguration.forSessionScope()
                    .isRouteRegistered(AdminView.class)) {
                RouteConfiguration.forSessionScope().setRoute("Категории",
                        AdminView.class, MainLayout.class);
            }
            if(!RouteConfiguration.forSessionScope()
                    .isRouteRegistered(UsersView.class))
                RouteConfiguration.forSessionScope().setRoute("Пользователи",
                        UsersView.class, MainLayout.class);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // User can quickly activate logout with Ctrl+L
        attachEvent.getUI().addShortcutListener(() -> logout(), Key.KEY_L,
                KeyModifier.CONTROL);

        if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {

            // Create extra navigation target for admins
            registerAdminViewsIfApplicable(accessControl);

            // The link can only be created now, because the RouterLink checks
            // that the target is valid.
            addToDrawer(createMenuLink(AdminView.class, "Категории",
                    VaadinIcon.DOCTOR.create()));

            addToDrawer(createMenuLink(UsersView.class, "Пользователи",
                    VaadinIcon.USER.create()));
        }

        // Finally, add logout button for all users
        addToDrawer(logoutButton);
    }

}
