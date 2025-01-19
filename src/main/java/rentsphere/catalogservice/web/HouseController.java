package rentsphere.catalogservice.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rentsphere.catalogservice.domain.House;
import rentsphere.catalogservice.domain.HouseService;

@RestController
@RequestMapping("houses")
public class HouseController {
    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping
    public Iterable<House> getAll() {
        return houseService.viewHouseList();
    }

    @GetMapping("{code}")
    public House getByCode(@PathVariable String code) {
        return houseService.viewHouseDetails(code);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public House post(@Valid @RequestBody House house) {
        return houseService.addHouseToCatalog(house);
    }

    @DeleteMapping("{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String code) {
        houseService.removeHouseFromCatalog(code);
    }

    @PutMapping("{code}")
    public House put(@PathVariable String code, @Valid @RequestBody House house) {
        return houseService.updateHouseDetails(code, house);
    }
}
