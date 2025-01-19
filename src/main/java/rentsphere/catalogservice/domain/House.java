package rentsphere.catalogservice.domain;

import jakarta.validation.constraints.*;

public record House(
        @NotBlank(message = "The house reference code must be defined.")
        @Pattern(
                regexp = "^([0-9]{9})$",
                message = "The house reference code format must be valid."
        )
        String houseCode,

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
        boolean laundry
) {}