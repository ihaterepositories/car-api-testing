package org.example.carapi;

/*
  @author   Oleh
  @project   CarApi
  @class  RepositoryTest
  @version  1.0.0 
  @since 19.10.2024 - 17:38
*/

import org.example.carapi.model.Car;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.example.carapi.repository.CarRepository;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataMongoTest
public class RepositoryTest {

    @Autowired
    CarRepository underTest;

    @BeforeEach
    void setUp() {
        Car car1 = new Car("1", "Toyota", "Corolla", 1000);
        Car car2 = new Car("2", "Honda", "Civic", 1000);
        Car car3 = new Car("3", "Ford", "Mustang", 1000);
        underTest.saveAll(List.of(car1, car2, car3));
    }

    // All cars with year <= 1800 is using for testing!
    @AfterEach
    void tearDown() {
        List<Car> carsToDelete = underTest.findAll().stream()
                .filter(car -> car.getYear() <= 1800)
                .toList();
        underTest.deleteAll(carsToDelete);
    }

    //TESTS

    @Test
    void testSetShouldContains_3_Records_ToTest() {
        List<Car> cars = underTest.findAll();
        assertEquals(3, cars.size());
    }

    @Test
    void shouldGiveIdForNewRecord() {
        // given
        Car car = new Car("Tesla", "Model S", 1700);
        // when
        underTest.save(car);
        Car carFromDb = underTest.findAll().stream()
                .filter(c -> c.getModel().equals("Model S"))
                .findFirst().orElse(null);
        // then
        assertNotNull(carFromDb);
        assertNotNull(carFromDb.getId());
        assertFalse(carFromDb.getId().isEmpty());
        assertEquals(24, carFromDb.getId().length()); // перевірка, що це ObjectId
    }

    @Test
    void shouldFindCarsByBrand() {
        // when
        List<Car> toyotaCars = underTest.findByBrand("Toyota");
        // then
        assertEquals(1, toyotaCars.size());
        assertEquals("Corolla", toyotaCars.get(0).getModel());
    }

    @Test
    void shouldFindCarsByYearGreaterThan() {
        // when
        List<Car> recentCars = underTest.findByYearGreaterThan(900);
        // then
        assertEquals(3, recentCars.size());
        assertTrue(recentCars.stream().allMatch(car -> car.getYear() > 900));
    }

    @Test
    void shouldDeleteCarByModel() {
        // given
        String modelToDelete = "Civic";
        // when
        Car carToDelete = underTest.findByModel(modelToDelete);
        underTest.delete(carToDelete);
        Car deletedCar = underTest.findByModel(modelToDelete);
        // then
        assertNull(deletedCar);
    }
}
