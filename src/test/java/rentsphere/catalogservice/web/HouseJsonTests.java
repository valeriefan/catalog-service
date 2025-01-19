package rentsphere.catalogservice.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import rentsphere.catalogservice.domain.House;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class HouseJsonTests {

    @Autowired
    private JacksonTester<House> json;

    @Test
    void testSerialize() throws Exception {
        var house = House.of("123456789", "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        var jsonContent = json.write(house);
        assertThat(jsonContent).extractingJsonPathStringValue("@.code")
                .isEqualTo(house.code());
        assertThat(jsonContent).extractingJsonPathStringValue("@.name")
                .isEqualTo(house.name());
        assertThat(jsonContent).extractingJsonPathStringValue("@.city")
                .isEqualTo(house.city());
        assertThat(jsonContent).extractingJsonPathStringValue("@.state")
                .isEqualTo(house.state());
        assertThat(jsonContent).extractingJsonPathStringValue("@.photo")
                .isEqualTo(house.photo());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.availableUnits")
                .isEqualTo((house.availableUnits()));
        assertThat(jsonContent).extractingJsonPathBooleanValue("@.wifi")
                .isEqualTo(house.wifi());
        assertThat(jsonContent).extractingJsonPathBooleanValue("@.laundry")
                .isEqualTo(house.laundry());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                    "code": "123456789",
                	"name": "Acme Fresh Start Housing",\s
                    "city": "Chicago",\s
                    "state": "IL",
                    "photo": "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                    "availableUnits": 4,
                    "wifi": true,
                    "laundry": true
                }
                """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(House.of("123456789", "Acme Fresh Start Housing",
                        "Chicago",
                        "IL",
                        "https://angular.dev/assets/images/tutorials/common/b" +
                                "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                        4,
                        true,
                        true
                ));
    }
}
