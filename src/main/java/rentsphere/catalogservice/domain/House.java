package rentsphere.catalogservice.domain;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.Instant;

public record House(

        @Id
        Long id,

        @NotBlank(message = "The house reference code must be defined.")
        @Pattern(
                regexp = "^([0-9]{9})$",
                message = "The house reference code format must be valid."
        )
        String code,

        @NotBlank(message = "The house name must be defined.")
        String name,

        @NotBlank(message = "The city must be defined.")
        String city,

        @NotBlank(message = "The state must be defined.")
        String state,

        @NotBlank(message = "The photo must be defined.")
        String photo,

        @NotNull(message = "The available units must be defined.")
        @PositiveOrZero(message = "The available units must be greater than or equal to zero.")
        int availableUnits,

        @NotNull(message = "The availability of wifi must be defined.")
        boolean wifi,

        @NotNull(message = "The availability of laundry must be defined.")
        boolean laundry,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate,

        @Version
        int version
) {
        public static House of(
                String code, String name, String city, String state, String photo,
                int availableUnits, boolean wifi, boolean laundry
        ) {
                return new House(
                        null, code, name, city, state, photo,
                        availableUnits, wifi, laundry, null, null, 0
                );
        }
}