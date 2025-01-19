package rentsphere.catalogservice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HouseServiceTest {

    @Mock
    private HouseRepository houseRepository;

    @InjectMocks
    private HouseService houseService;

    @Test
    void whenBookToCreateAlreadyExistsThenThrows() {
        var code = "123456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        when(houseRepository.existsByCode(code)).thenReturn(true);
        assertThatThrownBy(() -> houseService.addHouseToCatalog(houseToCreate))
                .isInstanceOf(HouseAlreadyExistsException.class)
                .hasMessage("A house with reference code " + code + " already exists.");
    }

    @Test
    void whenBookToReadDoesNotExistThenThrows() {
        var code = "123456789";
        when(houseRepository.findByCode(code)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> houseService.viewHouseDetails(code))
                .isInstanceOf(HouseNotFoundException.class)
                .hasMessage("The house with reference code " + code + " was not found.");
    }
}
