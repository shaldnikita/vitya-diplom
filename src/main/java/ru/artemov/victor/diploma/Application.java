package ru.artemov.victor.diploma;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import ru.artemov.victor.diploma.authentication.AccessControl;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.domain.entities.operation.OperationType;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.AnimalRepository;
import ru.artemov.victor.diploma.domain.repositories.OperationTypeRepository;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;
import ru.artemov.victor.diploma.ui.login.LoginScreen;

import java.util.HashSet;
import java.util.Set;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements VaadinServiceInitListener, CommandLineRunner {

    @Autowired
    private AccessControl accessControl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    @Autowired
    private AnimalRepository animalRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void serviceInit(ServiceInitEvent initEvent) {
        initEvent.getSource().addUIInitListener(uiInitEvent -> {
            uiInitEvent.getUI().addBeforeEnterListener(enterEvent -> {
                if (!accessControl.isUserSignedIn() && !LoginScreen.class
                        .equals(enterEvent.getNavigationTarget()))
                    enterEvent.rerouteTo(LoginScreen.class);
            });
        });
    }

    @Override
    public void run(String... args) {
        var operationType = new OperationType(
                "Удой молока", new HashSet<>()
        );
        operationTypeRepository.save(operationType);

        var user = new User(
                "Viktor", "Artemov", "va", "va", new HashSet<>(), new HashSet<>()
        );
        user = userRepository.save(user);
        user.getOperationTypes().add(operationType);
        userRepository.save(user);

        animalRepository.save(new Animal("Корова", "123", new HashSet<>()));

    }
}
