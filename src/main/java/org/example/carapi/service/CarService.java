package org.example.carapi.service;

/*
  @author   Oleh
  @project   CarApi
  @class  CarService
  @version  1.0.0 
  @since 30.09.2024 - 22:38
*/

import org.example.carapi.model.Car;
import org.example.carapi.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(String id) {
        return carRepository.findById(id);
    }

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public Car updateCar(String id, Car car) {
        car.setId(id);
        return carRepository.save(car);
    }

    public void deleteCar(String id) {
        carRepository.deleteById(id);
    }
}

