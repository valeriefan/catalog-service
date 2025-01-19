package rentsphere.catalogservice.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rentsphere.catalogservice.domain.HouseNotFoundException;
import rentsphere.catalogservice.domain.HouseService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@WebMvcTest(HouseController.class)
public class HouseControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HouseService houseService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String code = "123456789";
        given(houseService.viewHouseDetails(code)).willThrow(HouseNotFoundException.class);
        mockMvc
                .perform(get("/houses/" + code))
                .andExpect(status().isNotFound());
    }
}
