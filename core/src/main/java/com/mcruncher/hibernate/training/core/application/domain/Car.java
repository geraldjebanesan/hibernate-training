/*
 * Copyright (c) 2026. mCruncher Sdn Bhd, Cyberjaya, Malaysia.
 * All rights reserved.
 */

package com.mcruncher.hibernate.training.core.application.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

/**
 * @author Gerald Jebanesan
 * @since 10.5.0
 */

@Entity
@Data
@NoArgsConstructor
public class Car
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private int topSpeedInKilometersPerHour;

    /**
     * Hibernate does this for us
     * Cons:
     * - Hibernate specific
     * - Database specific because the column name needs to be as in DB (I created in H2)
     */
    @Formula("0.621371 * TOP_SPEED_IN_KILOMETERS_PER_HOUR")
    private double topSpeedInMilesPerHour;

    /**
     * This is the JPA compliant way to do
     * Defining a @Transient property and using the @Postload annotation on the method to compute
     */
    @Transient
    private double topSpeedInMetresPerSecond;

    @Enumerated(EnumType.STRING)
    private Type type;

    @PostLoad
    protected void calculateTopSpeedInMetres()
    {
        topSpeedInMetresPerSecond = Math.round(0.277778 * topSpeedInKilometersPerHour);
    }

    public enum Type {
        SEDAN, HATCHBACK, SUV, MPV, SPORTS
    }
}
