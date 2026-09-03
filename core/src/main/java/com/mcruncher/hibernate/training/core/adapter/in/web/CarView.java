/*
 * Copyright (c) 2026. mCruncher Sdn Bhd, Cyberjaya, Malaysia.
 * All rights reserved.
 */

package com.mcruncher.hibernate.training.core.adapter.in.web;

import com.mcruncher.hibernate.training.core.application.domain.Car;
import com.mcruncher.hibernate.training.core.application.port.out.persistence.CarPersistencePort;
import com.vaadin.copilot.theme.ApplicationTheme;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jspecify.annotations.NonNull;

/**
 * @author Gerald Jebanesan
 * @since 10.5.0
 */

@Route("")
@PageTitle("Car")
public class CarView extends VerticalLayout
{
    // Form components
    private final TextField brand;
    private final TextField model;
    private final TextField topSpeedInKilometersPerHour;
    private final Button saveButton;

    // Entity - Form Binder
    private final Binder<Car> binder = new Binder<>(Car.class);

    // Datatable
    private final Grid<Car> carGrid = new Grid<>(Car.class);

    private final transient CarPersistencePort carPersistencePort;

    public CarView(CarPersistencePort carPersistencePort)
    {
        super();
        this.carPersistencePort = carPersistencePort;

        this.brand = new TextField("Brand");
        this.model = new TextField("Model");
        this.topSpeedInKilometersPerHour = new TextField("Top Speed In Kilometers Per Hour");
        this.saveButton = new Button("Save Car");

        init();
        binder.bindInstanceFields(this);
        saveButton.addClickListener(event -> saveCar());
        carGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT, GridVariant.LUMO_COLUMN_BORDERS);
        refreshGrid();
        add(getTabs());
    }

    private void init()
    {
        this.setThemeName(ApplicationTheme.LUMO.name());
        this.setPadding(true);
        this.setMargin(true);
        this.setSizeFull();
    }

    private @NonNull TabSheet getTabs()
    {
        VerticalLayout createCarsTabContent = new VerticalLayout(createFormLayout(), saveButton);
        VerticalLayout viewCarsTabContent = new VerticalLayout(carGrid);
        viewCarsTabContent.setSizeFull();

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Create Car", createCarsTabContent);
        tabSheet.add("View Cars", viewCarsTabContent);
        tabSheet.addSelectedChangeListener(event -> refreshGrid());
        return tabSheet;
    }

    private @NonNull FormLayout createFormLayout()
    {
        FormLayout formLayout = new FormLayout(brand, model, topSpeedInKilometersPerHour);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        formLayout.setMaxWidth("400px");
        return formLayout;
    }

    private void saveCar()
    {
        try {
            Car newCar = new Car();
            binder.writeBean(newCar);
            carPersistencePort.create(newCar);

            Notification.show("Car saved successfully!");
            binder.readBean(new Car());
            refreshGrid();

        } catch (ValidationException e) {
            Notification.show("Please check the form for errors. " + e.getMessage());
        }
    }

    private void refreshGrid()
    {
        carGrid.setItems(carPersistencePort.fetchCars());
    }
}
