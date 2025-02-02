package rentsphere.catalogservice.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rentsphere.catalogservice.domain.House;
import rentsphere.catalogservice.domain.HouseService;

@RestController
@RequestMapping("houses")
public class HouseController {
    private static final Logger log =
            LoggerFactory.getLogger(HouseController.class);

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping
    public Iterable<House> getAll() {
        log.info("Fetching the list of houses in the catalog");
        return houseService.viewHouseList();
    }

    @GetMapping("{code}")
    public House getByCode(@PathVariable String code) {
        log.info("Fetching the house with reference code {} from the catalog", code);
        return houseService.viewHouseDetails(code);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public House post(@Valid @RequestBody House house) {
        log.info("Adding a new house to the catalog with reference code {}", house.code());
        return houseService.addHouseToCatalog(house);
    }

    @DeleteMapping("{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String code) {
        log.info("Deleting house with reference code {}", code);
        houseService.removeHouseFromCatalog(code);
    }

    @PutMapping("{code}")
    public House put(@PathVariable String code, @Valid @RequestBody House house) {
        log.info("Updating book with reference code {}", code);
        return houseService.updateHouseDetails(code, house);
    }
}
