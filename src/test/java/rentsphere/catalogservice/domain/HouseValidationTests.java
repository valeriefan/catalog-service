package rentsphere.catalogservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HouseValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        var house =
                House.of("123456789", "Acme Fresh Start Housing",
                        "Chicago",
                        "IL",
                        "https://angular.dev/assets/images/tutorials/common/b" +
                                "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                        4,
                        true,
                        true
                        );
        Set<ConstraintViolation<House>> violations = validator.validate(house);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenCodeNotDefinedThenValidationFails() {
        var house =
                House.of("", "Acme Fresh Start Housing",
                        "Chicago",
                        "IL",
                        "https://angular.dev/assets/images/tutorials/common/b" +
                                "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                        4,
                        true,
                        true
                );
        Set<ConstraintViolation<House>> violations = validator.validate(house);
        assertThat(violations).hasSize(2);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(constraintViolationMessages)
                .contains("The house reference code must be defined.")
                .contains("The house reference code format must be valid.");
    }

    @Test
    void whenCodeDefinedButIncorrectThenValidationFails() {
        var house =
                House.of("1111", "Acme Fresh Start Housing",
                        "Chicago",
                        "IL",
                        "https://angular.dev/assets/images/tutorials/common/be" +
                                "rnard-hermant-CLKGGwIBTaY-unsplash.jpg",
                        4,
                        true,
                        true
                );
        Set<ConstraintViolation<House>> violations = validator.validate(house);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The house reference code format must be valid.");
    }

    @Test
    void whenNameIsNotDefinedThenValidationFails() {
        var house =
                House.of("123456789", "",
                        "Chicago",
                        "IL",
                        "https://angular.dev/assets/images/tutorials/common/berna" +
                                "rd-hermant-CLKGGwIBTaY-unsplash.jpg",
                        4,
                        true,
                        true
                );
        Set<ConstraintViolation<House>> violations = validator.validate(house);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The house name must be defined.");
    }

    @Test
    void whenCityIsNotDefinedThenValidationFails() {
        var house =
                House.of("123456789", "Acme Fresh Start Housing",
                        "",
                        "IL",
                        "https://angular.dev/assets/images/tutorials/common/" +
                                "bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                        4,
                        true,
                        true
                );
        Set<ConstraintViolation<House>> violations = validator.validate(house);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The city must be defined.");
    }

    @Test
    void whenStateIsNotDefinedThenValidationFails() {
        var house =
                House.of("123456789", "Acme Fresh Start Housing",
                        "Chicago",
                        "",
                        "https://angular.dev/assets/images/tutorials/" +
                                "common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                        4,
                        true,
                        true
                );
        Set<ConstraintViolation<House>> violations = validator.validate(house);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The state must be defined.");
    }

    @Test
    void whenPhotoIsNotDefinedThenValidationFails() {
        var house =
                House.of("123456789", "Acme Fresh Start Housing",
                        "Chicago",
                        "IL",
                        "",
                        4,
                        true,
                        true
                );
        Set<ConstraintViolation<House>> violations = validator.validate(house);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The photo must be defined.");
    }

    @Test
    void whenAvailableUnitsIsNotDefinedThenValidationFails() {
        var house =
                House.of("123456789", "Acme Fresh Start Housing",
                        "Chicago",
                        "IL",
                        "https://angular.dev/assets/images/tutorials" +
                                "/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                        -4,
                        true,
                        true
                );
        Set<ConstraintViolation<House>> violations = validator.validate(house);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The available units must be greater than or equal to zero.");
    }

}
