/*
 * Copyright (c) 2026. mCruncher Sdn Bhd, Cyberjaya, Malaysia.
 * All rights reserved.
 */
package com.mcruncher.hibernate.training.core.application.port.out.persistence;

import com.mcruncher.hibernate.training.core.application.domain.Car;

import java.util.List;

/**
 * @author Gerald Jebanesan
 * @since 10.5.0
 */

public interface CarPersistencePort
{
    void create(Car car);

    void delete(Car car);

    List<Car> fetchCars();
}
