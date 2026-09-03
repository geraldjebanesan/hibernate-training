/*
 * Copyright (c) 2026. mCruncher Sdn Bhd, Cyberjaya, Malaysia.
 * All rights reserved.
 */

package com.mcruncher.hibernate.training.core.adapter.out.persistence.repository;

import com.mcruncher.hibernate.training.core.application.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gerald Jebanesan
 * @since 10.5.0
 */

public interface CarRepository extends JpaRepository<Car, Integer>
{
}
