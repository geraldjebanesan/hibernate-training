/*
 * Copyright (c) 2026. mCruncher Sdn Bhd, Cyberjaya, Malaysia.
 * All rights reserved.
 */

package com.mcruncher.hibernate.training.core.adapter.in.web;

import com.mcruncher.hibernate.training.core.application.domain.Car;
import com.mcruncher.hibernate.training.core.application.port.out.persistence.CarPersistencePort;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jspecify.annotations.NonNull;

/**
 * @author Gerald Jebanesan
 * @since 10.5.0
 */

@Route("")
@PageTitle("Car")
public class CarView extends VerticalLayout
{
    private final TextField brand = new TextField("Brand");
    private final TextField model = new TextField("Model");
    private final Select<Car.Type> type = new Select<>("Type", Car.Type.values());
    private final IntegerField topSpeedInKilometersPerHour = new IntegerField("Top Speed (km/h)");
    private final Button saveButton = new Button("Save Car", VaadinIcon.PLUS.create());

    private final Binder<Car> binder = new Binder<>(Car.class);
    private final Grid<Car> carGrid = new Grid<>(Car.class, false);

    private final transient CarPersistencePort carPersistencePort;

    public CarView(CarPersistencePort carPersistencePort)
    {
        super();
        this.carPersistencePort = carPersistencePort;

        initLayout();
        configureForm();
        configureGrid();
        configureBinder();
        refreshGrid();

        add(createHeader(), getTabs());
    }

    private void initLayout()
    {
        this.setPadding(true);
        this.setSpacing(true);
        this.setSizeFull();
        this.setMaxWidth("1200px");
        this.getStyle().set("margin", "0 auto");
    }

    private VerticalLayout createHeader()
    {
        H2 title = new H2("Car Fleet Management");
        title.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.NONE);

        Paragraph subtitle = new Paragraph("Manage and monitor vehicle speeds and performance metrics.");
        subtitle.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.Margin.Top.XSMALL);

        VerticalLayout header = new VerticalLayout(title, subtitle);
        header.setPadding(false);
        header.setSpacing(false);
        return header;
    }

    private void configureForm()
    {
        brand.setPlaceholder("e.g. BMW");
        brand.setRequired(true);
        model.setPlaceholder("e.g. M3");
        model.setRequired(true);
        type.setRequiredIndicatorVisible(true);
        topSpeedInKilometersPerHour.setPlaceholder("e.g. 280");

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClassName(LumoUtility.Margin.Top.MEDIUM);
        saveButton.addClickListener(_ -> saveCar());
    }

    private void configureGrid()
    {
        carGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER);
        carGrid.addColumn(Car::getId).setHeader("ID").setAutoWidth(true);
        carGrid.addColumn(Car::getBrand).setHeader("Brand").setSortable(true);
        carGrid.addColumn(Car::getModel).setHeader("Model").setSortable(true);
        carGrid.addColumn(Car::getType).setHeader("Type").setSortable(true);
        carGrid.addColumn(Car::getTopSpeedInKilometersPerHour).setHeader("Speed (km/h)").setSortable(true);
        carGrid.addColumn(car -> String.format("%.2f m/s", car.getTopSpeedInMetresPerSecond())).setHeader("Speed (m/s)");
        carGrid.addColumn(car -> String.format("%.2f mph", car.getTopSpeedInMilesPerHour())).setHeader("Speed (mph)");
        addDeleteColumn();
    }

    private void addDeleteColumn()
    {
        carGrid.addComponentColumn(car -> {
            Button deleteButton = new Button(VaadinIcon.TRASH.create(), _ -> deleteCar(car));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            return deleteButton;
        }).setHeader("Actions").setAutoWidth(true);
    }

    private void configureBinder()
    {
        binder.forField(brand)
                .asRequired("Brand is required")
                .bind(Car::getBrand, Car::setBrand);

        binder.forField(model)
                .asRequired("Model is required")
                .bind(Car::getModel, Car::setModel);

        binder.forField(type)
                .asRequired("Type is required")
                .bind(Car::getType, Car::setType);

        binder.forField(topSpeedInKilometersPerHour)
                .asRequired("Top speed is required")
                .withValidator(speed -> speed != null && speed > 0, "Speed must be greater than 0")
                .bind(Car::getTopSpeedInKilometersPerHour, Car::setTopSpeedInKilometersPerHour);
    }

    private @NonNull TabSheet getTabs()
    {
        VerticalLayout createCard = new VerticalLayout(createFormLayout(), saveButton);
        styleAsCard(createCard);

        VerticalLayout viewCard = new VerticalLayout(carGrid);
        styleAsCard(viewCard);
        viewCard.setSizeFull();

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Create Car", createCard);
        tabSheet.add("View Cars", viewCard);
        tabSheet.addSelectedChangeListener(_ -> refreshGrid());
        return tabSheet;
    }

    private @NonNull FormLayout createFormLayout()
    {
        FormLayout formLayout = new FormLayout(brand, model, type, topSpeedInKilometersPerHour);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        formLayout.setMaxWidth("500px");
        return formLayout;
    }

    private void styleAsCard(VerticalLayout layout)
    {
        layout.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Padding.LARGE
        );
    }

    private void saveCar()
    {
        try {
            Car newCar = new Car();
            binder.writeBean(newCar);
            carPersistencePort.create(newCar);

            showNotification("Car saved successfully!", NotificationVariant.LUMO_SUCCESS);
            binder.readBean(new Car());
            refreshGrid();
        } catch (ValidationException e) {
            showNotification("Please complete all required fields correctly.", NotificationVariant.LUMO_ERROR);
        }
    }

    private void deleteCar(Car car)
    {
        carPersistencePort.delete(car);
        showNotification("Car deleted successfully!", NotificationVariant.LUMO_SUCCESS);
        refreshGrid();
    }

    private void showNotification(String message, NotificationVariant colour)
    {
        Notification notification = new Notification(new Span(message));
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.addThemeVariants(colour);
        notification.open();
    }

    private void refreshGrid()
    {
        carGrid.setItems(carPersistencePort.fetchCars());
    }
}
