package ru.artemov.victor.diploma.ui.users;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.artemov.victor.diploma.domain.entities.user.User;
import ru.artemov.victor.diploma.domain.repositories.UserRepository;

@SpringComponent
@UIScope
public class UsersDataProvider extends CallbackDataProvider<User, Void> {


    public UsersDataProvider(UserRepository userRepository) {
        super(query -> {
                    query.getLimit();
                    query.getOffset();
                    return userRepository.findAll().stream();
                },
                query -> (int) userRepository.count());
    }

}
