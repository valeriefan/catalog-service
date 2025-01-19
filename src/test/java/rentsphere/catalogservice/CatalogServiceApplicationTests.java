package rentsphere.catalogservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import rentsphere.catalogservice.domain.House;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenGetRequestWithIdThenHouseReturned() {
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
        House expectedHouse = webTestClient
                .post()
                .uri("/houses")
                .bodyValue(houseToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(House.class).value(house -> assertThat(house).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/houses/" + code)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(House.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assert expectedHouse != null;
                    assertThat(actualBook.code()).isEqualTo(expectedHouse.code());
                });
    }

    @Test
    void whenPostRequestThenHouseCreated() {
        var expectedHouse = House.of("223456789", "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );

        webTestClient
                .post()
                .uri("/houses")
                .bodyValue(expectedHouse)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(House.class).value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.code()).isEqualTo(expectedHouse.code());
                });
    }

    @Test
    void whenPutRequestThenHouseUpdated() {
        var code = "323456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        House createdHouse = webTestClient
                .post()
                .uri("/houses")
                .bodyValue(houseToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(House.class).value(book -> assertThat(book).isNotNull())
                .returnResult().getResponseBody();
        assert createdHouse != null;
        var bookToUpdate = House.of(
                createdHouse.code(),
                createdHouse.name(),
                createdHouse.city(),
                createdHouse.state(),
                createdHouse.photo(),
                createdHouse.availableUnits(),
                createdHouse.wifi(),
                createdHouse.laundry()
                );

        webTestClient
                .put()
                .uri("/houses/" + code)
                .bodyValue(bookToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(House.class).value(actualHouse -> {
                    assertThat(actualHouse).isNotNull();
                    assertThat(actualHouse.availableUnits()).isEqualTo(bookToUpdate.availableUnits());
                });
    }

    @Test
    void whenDeleteRequestThenHouseDeleted() {
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
        webTestClient
                .post()
                .uri("/houses")
                .bodyValue(houseToCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .delete()
                .uri("/houses/" + code)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/houses/" + code)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(errorMessage ->
                        assertThat(errorMessage).isEqualTo("The house with reference code " + code +
                                " was not found.")
                );
    }

}
