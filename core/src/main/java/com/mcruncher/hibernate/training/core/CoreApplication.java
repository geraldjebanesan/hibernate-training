package com.mcruncher.hibernate.training.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class CoreApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(CoreApplication.class, args);
    }
}
