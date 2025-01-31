package rentsphere.catalogservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import rentsphere.catalogservice.domain.House;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Testcontainers
class CatalogServiceApplicationTests {

    private static KeycloakToken bjornTokens;
    private static KeycloakToken isabelleTokens;



    @Autowired
    private WebTestClient webTestClient;

    @Container
    private static final KeycloakContainer keycloakContainer =
            new KeycloakContainer("quay.io/keycloak/keycloak")
                    .withRealmImportFile("/test-realm-config.json");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realms/RentSphere");
    }

    @BeforeAll
    static void generateAccessTokens() {
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl()
                        + "realms/RentSphere/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        isabelleTokens = authenticateWith(
                "isabelle", "password", webClient);
        bjornTokens = authenticateWith(
                "bjorn", "password", webClient);
    }

    @Test
    void whenGetRequestWithIdThenBookReturned() {
        String code = "133456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        House expectedHouse = webTestClient
                .post()
                .uri("/houses")
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .bodyValue(houseToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(House.class).value(book -> assertThat(book).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/houses/" + code)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(House.class).value(actualHouse -> {
                    assertThat(actualHouse).isNotNull();
                    assert expectedHouse != null;
                    assertThat(actualHouse.code()).isEqualTo(expectedHouse.code());
                });
    }

    @Test
    void whenPostRequestThenBookCreated() {
        String code = "523456789";
        var expectedHouse = House.of(code, "Acme Fresh Start Housing",
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
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .bodyValue(expectedHouse)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(House.class).value(actualHouse -> {
                    assertThat(actualHouse).isNotNull();
                    assertThat(actualHouse.code()).isEqualTo(expectedHouse.code());
                });
    }

    @Test
    void whenPostRequestUnauthenticatedThen401() {
        String code = "123456789";
        var expectedHouse = House.of(code, "Acme Fresh Start Housing",
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
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenPostRequestUnauthorizedThen403() {
        String code = "723456789";
        var expectedHouse = House.of(code, "Acme Fresh Start Housing",
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
                .headers(headers -> headers.setBearerAuth(bjornTokens.accessToken()))
                .bodyValue(expectedHouse)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenPutRequestThenBookUpdated() {
        String code = "123456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        House createdHouse = webTestClient
                .post()
                .uri("/houses")
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .bodyValue(houseToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(House.class).value(book -> assertThat(book).isNotNull())
                .returnResult().getResponseBody();
        assert createdHouse != null;
        var bookToUpdate = new House(createdHouse.id(),
                createdHouse.code(), createdHouse.name(),
                createdHouse.city(),
                createdHouse.state(),
                createdHouse.photo(),
                20,
                createdHouse.wifi(),
                createdHouse.laundry(),
                createdHouse.createdDate(),
                createdHouse.lastModifiedDate(),
                createdHouse.version());

        webTestClient
                .put()
                .uri("/houses/" + code)
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .bodyValue(bookToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(House.class).value(actualHouse -> {
                    assertThat(actualHouse).isNotNull();
                    assertThat(actualHouse.availableUnits()).isEqualTo(bookToUpdate.availableUnits());
                });
    }

    @Test
    void whenDeleteRequestThenBookDeleted() {
        String code = "923456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
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
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .bodyValue(houseToCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .delete()
                .uri("/houses/" + code)
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/houses/" + code)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(errorMessage ->
                        assertThat(errorMessage).isEqualTo("The house with reference code " + code + " was not found.")
                );
    }

    private static KeycloakToken authenticateWith(
            String username, String password, WebClient webClient
    ) {
        return webClient
                .post()
                .body(
                        BodyInserters.fromFormData("grant_type", "password")
                                .with("client_id", "rentsphere-test")
                                .with("username", username)
                                .with("password", password)
                )
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }

    private record KeycloakToken(String accessToken) {
        @JsonCreator
        private KeycloakToken(
                @JsonProperty("access_token") final String accessToken
        ) {
            this.accessToken = accessToken;
        }
    }

}
