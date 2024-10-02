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

public interface CarRepository extends MongoRepository<Car, String> {

}

