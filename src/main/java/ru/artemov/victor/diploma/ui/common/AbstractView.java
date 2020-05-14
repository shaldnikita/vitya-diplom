package ru.artemov.victor.diploma.ui.common;


import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.HasUrlParameter;
import ru.artemov.victor.diploma.domain.entities.AbstractEntity;

public abstract class AbstractView<T extends AbstractEntity> extends HorizontalLayout implements HasUrlParameter<String> {

    protected final DataProvider<T, Void> dataProvider;

    protected final AbstractViewLogic<T> viewLogic;

    public AbstractView(DataProvider<T, Void> dataProvider, AbstractViewLogic<T> viewLogic) {
        this.dataProvider = dataProvider;
        this.viewLogic = viewLogic;
    }
}
