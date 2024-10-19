package org.example.carapi.repository;

/*
  @author   Oleh
  @project   CarApi
  @class  CarRepository
  @version  1.0.0 
  @since 30.09.2024 - 22:37
*/

import org.example.carapi.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CarRepository extends MongoRepository<Car, String> {

    List<Car> findByBrand(String brand);
    Car findByModel(String model);
    List<Car> findByYearGreaterThan(int year);

}

