package rentsphere.catalogservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rentsphere.catalogservice.config.SecurityConfig;
import rentsphere.catalogservice.domain.House;
import rentsphere.catalogservice.domain.HouseNotFoundException;
import rentsphere.catalogservice.domain.HouseService;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HouseController.class)
@Import(SecurityConfig.class)
public class HouseControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    JwtDecoder jwtDecoder;

    @MockitoBean
    private HouseService houseService;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    void whenGetBookExistingAndAuthenticatedThenShouldReturn200() throws Exception {
        String code = "123456789";
        var expectedHouse = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        given(houseService.viewHouseDetails(code)).willReturn(expectedHouse);
        mockMvc
                .perform(get("/houses/" + code)
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetBookExistingAndNotAuthenticatedThenShouldReturn200() throws Exception {
        String code = "123456789";
        var expectedHouse = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        given(houseService.viewHouseDetails(code)).willReturn(expectedHouse);
        mockMvc
                .perform(get("/houses/" + code))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetBookNotExistingAndAuthenticatedThenShouldReturn404() throws Exception {
        String code = "123456789";
        given(houseService.viewHouseDetails(code)).willThrow(HouseNotFoundException.class);
        mockMvc
                .perform(get("/houses/" + code)
                        .with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetBookNotExistingAndNotAuthenticatedThenShouldReturn404() throws Exception {
        String code = "123456789";
        given(houseService.viewHouseDetails(code)).willThrow(HouseNotFoundException.class);
        mockMvc
                .perform(get("/houses/" + code))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteHouseWithEmployeeRoleThenShouldReturn204()
            throws Exception
    {
        String code = "123456789";
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/houses/" + code)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void whenDeleteHouseWithCustomerRoleThenShouldReturn403()
            throws Exception
    {
        String code = "123456789";
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/houses/" + code)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void whenDeleteHouseNotAuthenticatedThenShouldReturn401()
            throws Exception
    {
        String code = "123456789";
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/houses/" + code))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void whenPostBookWithEmployeeRoleThenShouldReturn201() throws Exception {
        String code = "123456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        given(houseService.addHouseToCatalog(houseToCreate)).willReturn(houseToCreate);
        mockMvc
                .perform(post("/houses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(houseToCreate))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(status().isCreated());
    }

    @Test
    void whenPostBookWithCustomerRoleThenShouldReturn403() throws Exception {
        String code = "123456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        given(houseService.addHouseToCatalog(houseToCreate)).willReturn(houseToCreate);
        mockMvc
                .perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(houseToCreate))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPostBookAndNotAuthenticatedThenShouldReturn403() throws Exception {
        String code = "123456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        mockMvc
                .perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(houseToCreate)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenPutBookWithEmployeeRoleThenShouldReturn200() throws Exception {
        String code = "123456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        given(houseService.addHouseToCatalog(houseToCreate)).willReturn(houseToCreate);
        mockMvc
                .perform(put("/houses/" + code)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(houseToCreate))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutBookWithCustomerRoleThenShouldReturn403() throws Exception {
        String code = "123456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        given(houseService.addHouseToCatalog(houseToCreate)).willReturn(houseToCreate);
        mockMvc
                .perform(put("/houses/" + code)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(houseToCreate))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPutBookAndNotAuthenticatedThenShouldReturn401() throws Exception {
        String code = "123456789";
        var houseToCreate = House.of(code, "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/bernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        mockMvc
                .perform(put("/houses/" + code)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(houseToCreate)))
                .andExpect(status().isUnauthorized());
    }
}
