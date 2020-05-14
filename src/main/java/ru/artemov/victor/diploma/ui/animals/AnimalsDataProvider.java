package ru.artemov.victor.diploma.ui.animals;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.web.context.annotation.SessionScope;
import ru.artemov.victor.diploma.domain.entities.animal.Animal;
import ru.artemov.victor.diploma.domain.repositories.AnimalRepository;


@SpringComponent
@SessionScope
public class AnimalsDataProvider extends CallbackDataProvider<Animal, Void> {


    public AnimalsDataProvider(AnimalRepository repository) {
        super(query -> {
                    query.getLimit();
                    query.getOffset();
                    return repository.findAll().stream();
                },
                query -> (int) repository.count());
    }

}