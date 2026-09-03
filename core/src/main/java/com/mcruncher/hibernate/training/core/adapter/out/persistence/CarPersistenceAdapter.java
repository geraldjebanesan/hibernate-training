/*
 * Copyright (c) 2026. mCruncher Sdn Bhd, Cyberjaya, Malaysia.
 * All rights reserved.
 */

package com.mcruncher.hibernate.training.core.adapter.out.persistence;

import com.mcruncher.hibernate.training.core.application.domain.Car;
import com.mcruncher.hibernate.training.core.application.port.out.persistence.CarPersistencePort;
import com.mcruncher.hibernate.training.core.adapter.out.persistence.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Gerald Jebanesan
 * @since 10.5.0
 */

@Service
public class CarPersistenceAdapter implements CarPersistencePort
{
    private final CarRepository carRepository;

    public CarPersistenceAdapter(CarRepository carRepository)
    {
        this.carRepository = carRepository;
    }

    @Override
    public void create(Car car)
    {
        carRepository.save(car);
    }

    @Override
    public void delete(Car car)
    {
        carRepository.delete(car);
    }

    @Override
    public List<Car> fetchCars()
    {
        return carRepository.findAll();
    }
}
