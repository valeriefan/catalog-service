package rentsphere.catalogservice.domain;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import rentsphere.catalogservice.config.DataConfig;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration")
public class HouseRepositoryJdbcTests {
    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findAllHouses() {
        var house1 = House.of("623456789", "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        var house2 = House.of("723456789", "A113 Transitional Housing",
                "Santa Monica",
                "CA",
                "https://angular.dev/assets/images/tutorials/common/brandon-griggs-" +
                        "wR11KBaB86U-unsplash.jpg",
                0,
                false,
                true
        );
        jdbcAggregateTemplate.insert(house1);
        jdbcAggregateTemplate.insert(house2);

        Iterable<House> actualHouses = houseRepository.findAll();

        assertThat(StreamSupport.stream(actualHouses.spliterator(), true)
                .filter(house -> house.code().equals(house1.code()) || house.code().equals(house2.code()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void findHouseByIsbnWhenExisting() {
        var code = "823456789";
        var house = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        jdbcAggregateTemplate.insert(house);
        Optional<House> actualHouse = houseRepository.findByCode(code);

        assertThat(actualHouse).isPresent();
        assertThat(actualHouse.get().code()).isEqualTo(house.code());
    }

    @Test
    void findHouseByCodeWhenNotExisting() {
        Optional<House> actualBook = houseRepository.findByCode("9234561238");
        assertThat(actualBook).isEmpty();
    }

    @Test
    void existsByCodeWhenExisting() {
        var code = "423456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        jdbcAggregateTemplate.insert(houseToCreate);

        boolean existing = houseRepository.existsByCode(code);

        assertThat(existing).isTrue();
    }

    @Test
    void existsByCodeWhenNotExisting() {
        boolean existing = houseRepository.existsByCode("9234561240");
        assertThat(existing).isFalse();
    }

    @Test
    void deleteByCode() {
        var code = "523456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        var persistedHouse = jdbcAggregateTemplate.insert(houseToCreate);

        houseRepository.deleteByCode(code);

        assertThat(jdbcAggregateTemplate.findById(persistedHouse.id(), House.class)).isNull();
    }

    @Test
    void whenCreateBookNotAuthenticatedThenNoAuditMetadata() {
        var code = "523456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        var createdHouse = houseRepository.save(houseToCreate);

        assertThat(createdHouse.createdBy()).isNull();
        assertThat(createdHouse.lastModifiedBy()).isNull();
    }

    @Test
    @WithMockUser("john")
    void whenCreateBookAuthenticatedThenAuditMetadata() {
        var code = "523456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        var createdHouse = houseRepository.save(houseToCreate);

        assertThat(createdHouse.createdBy())
                .isEqualTo("john");
        assertThat(createdHouse.lastModifiedBy())
                .isEqualTo("john");
    }
}