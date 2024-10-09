package org.example.carapi;

/*
  @author   Oleh
  @project   CarApi
  @class  CarApiArchitectureTests
  @version  1.0.0 
  @since 09.10.2024 - 19:34
*/

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@SpringBootTest
class CarApiArchitectureTests {

    private JavaClasses applicationClasses;

    @BeforeEach
    void initialize() {
        applicationClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("org.example.carapi");
    }

    // 1. Перевірка дотримання шарової архітектури
    @Test
    void shouldFollowLayerArchitecture()  {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")
                //
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                //
                .check(applicationClasses);
    }

    // 2. Контролери не повинні звертатись один до одного
    @Test
    void controllersShouldNotDependOnOtherControllers() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                .check(applicationClasses);
    }

    // 3. Сервіси не повинні звертатись до контролерів
    @Test
    void servicesShouldNotAccessControllers() {
        noClasses()
                .that().resideInAPackage("..service..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                .check(applicationClasses);
    }

    // 4. Репозиторії не повинні залежати від сервісів
    @Test
    void repositoriesShouldNotDependOnServices() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..service..")
                .check(applicationClasses);
    }

    // 5. Контролери повинні містити в назві "Controller"
    @Test
    void controllerClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(applicationClasses);
    }

    // 6. Контролери повинні бути анотовані @RestController
    @Test
    void controllerClassesShouldBeAnnotatedByRestController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .check(applicationClasses);
    }

    // 7. Репозиторії повинні бути інтерфейсами
    @Test
    void repositoryShouldBeInterfaces() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .beInterfaces()
                .check(applicationClasses);
    }

    // 8. Методи в контролерах повинні бути публічними
    @Test
    void controllerMethodsShouldBePublic() {
        methods()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..controller..")
                .should().bePublic()
                .check(applicationClasses);
    }

    // 9. Сервіси повинні бути анотовані @Service
    @Test
    void serviceClassesShouldBeAnnotatedWithService() {
        classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith(Service.class)
                .check(applicationClasses);
    }

    // 10. Репозиторії повинні бути анотовані @Repository
    @Test
    void repositoryClassesShouldBeAnnotatedWithRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().beAnnotatedWith(Repository.class)
                .check(applicationClasses);
    }

    // 11. Поля моделі Car не повинні бути публічними
    @Test
    void carModelFieldsShouldNotBePublic() {
        fields()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..model..")
                .should().notBePublic()
                .check(applicationClasses);
    }

    // 12. Класи моделі Car повинні бути у пакеті model
    @Test
    void carModelClassesShouldBeInModelPackage() {
        classes()
                .that().areAnnotatedWith(Entity.class)
                .should().resideInAPackage("..model..")
                .check(applicationClasses);
    }

    // 13. Сервіси повинні містити у імені "Service"
    @Test
    void serviceClassesShouldBeNamedXService() {
        classes()
                .that().resideInAPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .check(applicationClasses);
    }

    // 14. Репозиторії повинні містити у імені "Repository"
    @Test
    void repositoryClassesShouldBeNamedXRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().haveSimpleNameEndingWith("Repository")
                .check(applicationClasses);
    }

    // 15. Методи в сервісах повинні бути публічними
    @Test
    void serviceMethodsShouldBePublic() {
        methods()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..service..")
                .should().bePublic()
                .check(applicationClasses);
    }

    // 16. Поля в сервісах не повинні бути публічними
    @Test
    void serviceFieldsShouldNotBePublic() {
        fields()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..service..")
                .should().notBePublic()
                .check(applicationClasses);
    }

    // 17. Поля в репозиторіях не повинні бути публічними
    @Test
    void repositoryFieldsShouldNotBePublic() {
        fields()
                .that().areDeclaredInClassesThat()
                .resideInAPackage("..repository..")
                .should().notBePublic()
                .check(applicationClasses);
    }

    // 18. Контролери не повинні використовувати @Autowired на полях
    @Test
    void controllerFieldsShouldNotBeAutowired() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }

    // 19. Сервіси повинні звертатись лише до репозиторіїв та інших сервісів
    @Test
    void servicesShouldOnlyAccessRepositoriesAndServices() {
        classes()
                .that().resideInAPackage("..service..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage("..repository..", "..service..", "java..")
                .check(applicationClasses);
    }

    // 20. Репозиторії повинні звертатись лише до моделей
    @Test
    void repositoriesShouldOnlyAccessModels() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage("..model..", "java..")
                .check(applicationClasses);
    }
}

